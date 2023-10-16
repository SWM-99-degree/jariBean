package com.example.jariBean.service;

import com.example.jariBean.dto.cafe.CafeResDto.CafeDetailDto;
import com.example.jariBean.dto.cafe.CafeResDto.CafeDetailReserveDto;
import com.example.jariBean.dto.cafe.CafeResDto.CafeSummaryDto;
import com.example.jariBean.dto.reserved.ReservedResDto.TableReserveResDto;
import com.example.jariBean.dto.reserved.ReservedResDto.availableTime;
import com.example.jariBean.dto.table.TableResDto.TableDetailDto;
import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.Reserved;
import com.example.jariBean.entity.Table;
import com.example.jariBean.entity.TableClass;
import com.example.jariBean.handler.ex.CustomApiException;
import com.example.jariBean.handler.ex.CustomDBException;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.matching.MatchingRepository;
import com.example.jariBean.repository.reserved.ReservedRepository;
import com.example.jariBean.repository.table.TableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeService {
    private final CafeRepository cafeRepository;

    private final MatchingRepository matchingRepository;

    private final ReservedRepository reservedRepository;

    private final TableRepository tableRepository;

    public Page<CafeSummaryDto> getCafeByMatchingCount(Pageable pageable){
        try {
            List<String> cafeList = matchingRepository.findCafeIdSortedByCount(pageable);
            Page<Cafe> pagedCafes = cafeRepository.findByIds(cafeList, pageable);
            Page<CafeSummaryDto> cafeSummaryDtos = pagedCafes.map(cafe -> new CafeSummaryDto(cafe));
            return cafeSummaryDtos;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomDBException("DB에 조회하신 정보가 없습니다.");
        }
    }



    public CafeDetailReserveDto getCafeWithSearchingReserved(String cafeId, LocalDateTime reserveStartTime, LocalDateTime reserveEndTime, Integer seating, List<TableClass.TableOption> tableOptions,
                                                             Pageable pageable) {
        // select cafe
        CafeDetailReserveDto cafeDetailReserveDto = new CafeDetailReserveDto();
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new CustomDBException("id에 해당하는 Cafe가 존재하지 않습니다."));
        cafeDetailReserveDto.setCafeDetailDto(new CafeDetailDto(cafe));

        // select tables
        List<Table> tables = tableRepository.findByConditions(cafeId, seating, tableOptions);

        // sort by reserved with tableId
        Map<String, List<Reserved>> reservedListByTable = new LinkedHashMap<>();
        reservedRepository.findReservedByConditions(cafeId, reserveStartTime, seating, tableOptions).forEach(reserved -> {
            if (!reservedListByTable.containsKey(reserved.getTable().getId())){
                reservedListByTable.put(reserved.getTable().getId(), new ArrayList<>());
            }
            reservedListByTable.get(reserved.getTable().getId()).add(reserved);
        });
        return fillReservedOnDto(cafe, cafeDetailReserveDto, reservedListByTable, tables, reserveStartTime, pageable);
    }


    public CafeDetailReserveDto getCafeWithTodayReserved(String cafeId, Pageable pageable){
        // select cafe
        CafeDetailReserveDto cafeDetailReserveDto = new CafeDetailReserveDto();
        LocalDateTime now = LocalDateTime.now();
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> new CustomDBException("id에 해당하는 Cafe가 존재하지 않습니다."));
        cafeDetailReserveDto.setCafeDetailDto(new CafeDetailDto(cafe));
        cafeDetailReserveDto.setTableReserveResDtoList(new ArrayList<>());

        // select table with condition
        List<Table> tables = tableRepository.findByCafeId(cafeId);

        // sort by reserved with tableId
        Map<String, List<Reserved>> reservedListByTable = new LinkedHashMap<>();
        reservedRepository.findTodayReservedById(cafeId, LocalDateTime.now()).forEach(reserved -> {
            if (!reservedListByTable.containsKey(reserved.getTable().getId())){
                reservedListByTable.put(reserved.getTable().getId(), new ArrayList<>());
            }
            reservedListByTable.get(reserved.getTable().getId()).add(reserved);
        });
        return fillReservedOnDto(cafe, cafeDetailReserveDto, reservedListByTable, tables, now, pageable);
    }

    public CafeDetailReserveDto fillReservedOnDto(Cafe cafe, CafeDetailReserveDto cafeDetailReserveDto, Map<String, List<Reserved>> reservedListByTable, List<Table> tables, LocalDateTime localDateTime, Pageable pageable){

        // pagination
        Integer size = pageable.getPageSize();
        Long offset = pageable.getOffset();
        if (offset.intValue() + size > tables.size()) {
            size = tables.size() - offset.intValue();
        }

        // reserved to Dto
        for (Table table : tables.subList(offset.intValue(), size)) {
            TableReserveResDto tableReserveResDto = new TableReserveResDto();
            TableDetailDto tableDetailDto = new TableDetailDto(table);
            tableReserveResDto.setTableDetailDto(tableDetailDto);
            List<availableTime> times = new ArrayList<>();
            System.out.println(cafe.getStartTime());
            System.out.println(cafe.getEndTime());
            LocalDateTime startTime = cafe.getStartTime().withMonth(localDateTime.getMonthValue()).withDayOfMonth(localDateTime.getDayOfMonth()).withYear(localDateTime.getYear());
            LocalDateTime endTime = cafe.getEndTime().withMonth(localDateTime.getMonthValue()).withDayOfMonth(localDateTime.getDayOfMonth()).withYear(localDateTime.getYear());

            // if not reserved
            if (reservedListByTable.get(table.getId()) == null ) {
                times.add(new availableTime(startTime, endTime));
                tableReserveResDto.setAvailableTimeList(times);
                cafeDetailReserveDto.addTable(tableReserveResDto);
                continue;
            }

            // if reserved
            for (Reserved reserved : reservedListByTable.get(table.getId())) {
                if (!startTime.isEqual(reserved.getStartTime())){
                    times.add(new availableTime(startTime, reserved.getStartTime()));
                }
                startTime = reserved.getEndTime();
            }
            if (!startTime.isEqual(endTime)){
                times.add(new availableTime(startTime, endTime));
            }

            tableReserveResDto.setAvailableTimeList(times);
            cafeDetailReserveDto.addTable(tableReserveResDto);
        }
        return cafeDetailReserveDto;

    }

    public CafeSummaryDto getSummaryByCafeId(String cafeId) {
        Cafe fCafe = cafeRepository.findById(cafeId)
                .orElseThrow(() -> new CustomApiException("cafeId에 해당하는 Cafe가 존재하지 않습니다."));
        return new CafeSummaryDto(fCafe);
    }
}
