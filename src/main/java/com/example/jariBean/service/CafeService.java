package com.example.jariBean.service;

import com.example.jariBean.dto.cafe.CafeResDto.CafeDetailDto;
import com.example.jariBean.dto.cafe.CafeResDto.CafeDetailReserveDto;
import com.example.jariBean.dto.cafe.CafeResDto.CafeSummaryDto;
import com.example.jariBean.dto.reserved.ReservedResDto.TableReserveResDto;
import com.example.jariBean.dto.reserved.ReservedResDto.availableTime;
import com.example.jariBean.dto.table.TableResDto.TableDetailDto;
import com.example.jariBean.entity.*;
import com.example.jariBean.handler.ex.CustomDBException;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.cafeOperatingTime.CafeOperatingTimeRepository;
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
import java.util.*;

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
        // searching by matching
        List<String> cafeList = matchingRepository.findCafeIdSortedByCount(pageable);
        Page<Cafe> coundtedCafes = cafeRepository.findByIds(cafeList, pageable);
        Page<CafeSummaryDto> cafeSummaryDtos = coundtedCafes.map(cafe -> new CafeSummaryDto(cafe));

        return cafeSummaryDtos;
    }


    public CafeDetailReserveDto getCafeWithSearchingReserved(String cafeId, LocalDateTime reserveStartTime, LocalDateTime reserveEndTime, Integer seating, List<TableClass.TableOption> tableOptions,
                                                             Pageable pageable) {
        // select cafe
        CafeDetailReserveDto cafeDetailReserveDto = new CafeDetailReserveDto();
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow();
        cafeDetailReserveDto.setCafeDetailDto(new CafeDetailDto(cafe));
        System.out.println(cafeId);

        // select tables
        List<Table> tables = tableRepository.findByConditions(cafeId, seating, tableOptions);
        for (Table table : tables) {
            System.out.println(table.getId());
        }

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
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow();
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
            LocalDateTime startTime = cafe.getStartTime().withDayOfMonth(localDateTime.getDayOfMonth()).withMonth(localDateTime.getMonthValue()).withYear(localDateTime.getYear());
            LocalDateTime endTime = cafe.getEndTime().withDayOfMonth(localDateTime.getDayOfMonth()).withMonth(localDateTime.getMonthValue()).withYear(localDateTime.getYear());

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

}
