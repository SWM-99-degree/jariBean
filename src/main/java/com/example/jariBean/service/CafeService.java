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



    public List<CafeSummaryDto> getCafeByMatchingCount(Pageable pageable){
        try {
            List<String> cafeList = matchingRepository.findCafeIdSortedByCount(pageable);
            List<CafeSummaryDto> cafeSummaryDtos = new ArrayList<>();
            cafeRepository.findByIds(cafeList, pageable).forEach(cafe -> cafeSummaryDtos.add(new CafeSummaryDto(cafe)));
            return cafeSummaryDtos;
        } catch (Exception e) {
            throw new CustomDBException("DB에 조회하신 정보가 없습니다.");
        }

    }

    public CafeDetailReserveDto getCafeWithSearchingReserved(String cafeId, LocalDateTime reserveStartTime, LocalDateTime reserveEndTime, Integer seating, List<TableClass.TableOption> tableOptions,
                                                             Pageable pageable) {

        CafeDetailReserveDto cafeDetailReserveDto = new CafeDetailReserveDto();
        try {
            Cafe cafe = cafeRepository.findById(cafeId).orElseThrow();
            cafeDetailReserveDto.setCafeDetailDto(new CafeDetailDto(cafe));

            List<Table> tables = tableRepository.findByConditions(cafeId, seating, tableOptions);


            Map<String, List<Reserved>> reservedListByTabldIdWithPagination = new LinkedHashMap<>();
            reservedRepository.findReservedByConditions(cafeId, reserveStartTime, reserveEndTime, seating, tableOptions).forEach(reserved ->
                    {
                        if (!reservedListByTabldIdWithPagination.containsKey(reserved.getTable().getId())){
                            reservedListByTabldIdWithPagination.put(reserved.getTable().getId(), new ArrayList<>());
                        }
                        reservedListByTabldIdWithPagination.get(reserved.getTable().getId()).add(reserved);
                    }
            );

            Integer size = pageable.getPageSize();
            Long offset = pageable.getOffset();
            if (offset.intValue() + size > tables.size()) {
                size = tables.size() - offset.intValue();
            }


            for (Table table : tables.subList(offset.intValue(), size)) {
                TableReserveResDto tableReserveResDto = new TableReserveResDto();
                TableDetailDto tableDetailDto = new TableDetailDto(table);
                tableReserveResDto.setTableDetailDto(tableDetailDto);
                List<availableTime> times = new ArrayList<>();
                LocalDateTime startTime = cafe.getStartTime().withDayOfMonth(reserveStartTime.getDayOfMonth()).withMonth(reserveEndTime.getMonthValue()).withYear(reserveStartTime.getYear());
                LocalDateTime endTime = cafe.getEndTime().withDayOfMonth(reserveStartTime.getDayOfMonth()).withMonth(reserveStartTime.getMonthValue()).withYear(reserveEndTime.getYear());

                if (reservedListByTabldIdWithPagination.get(table.getId()) == null ) {
                    times.add(new availableTime(startTime, endTime));
                    tableReserveResDto.setAvailableTimeList(times);
                    cafeDetailReserveDto.addTable(tableReserveResDto);
                    continue;
                }

                for (Reserved reserved : reservedListByTabldIdWithPagination.get(table.getId())) {
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
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomDBException("해당 되는 카페가 없습니다.");
        }

    }


    public CafeDetailReserveDto getCafeWithTodayReserved(String cafeId, Pageable pageable){
        CafeDetailReserveDto cafeDetailReserveDto = new CafeDetailReserveDto();
        LocalDateTime now = LocalDateTime.now();
        try {
            Cafe cafe = cafeRepository.findById(cafeId).orElseThrow();
            cafeDetailReserveDto.setCafeDetailDto(new CafeDetailDto(cafe));
            cafeDetailReserveDto.setTableReserveResDtoList(new ArrayList<>());
            List<Table> tables = tableRepository.findByCafeId(cafeId);

            Map<String, List<Reserved>> reservedListByTabldIdWithPagination = new LinkedHashMap<>();
            reservedRepository.findTodayReservedById(cafeId).forEach(reserved ->
                    {
                        if (!reservedListByTabldIdWithPagination.containsKey(reserved.getTable().getId())){
                            reservedListByTabldIdWithPagination.put(reserved.getTable().getId(), new ArrayList<>());
                        }
                        reservedListByTabldIdWithPagination.get(reserved.getTable().getId()).add(reserved);
                    }
            );

            Integer size = pageable.getPageSize();
            Long offset = pageable.getOffset();
            if (offset.intValue() + size > tables.size()) {
                size = tables.size() - offset.intValue();
            }


            for (Table table : tables.subList(offset.intValue(), size)) {
                TableReserveResDto tableReserveResDto = new TableReserveResDto();
                TableDetailDto tableDetailDto = new TableDetailDto(table);
                tableReserveResDto.setTableDetailDto(tableDetailDto);
                List<availableTime> times = new ArrayList<>();
                LocalDateTime startTime = cafe.getStartTime().withDayOfMonth(now.getDayOfMonth()).withMonth(now.getMonthValue()).withYear(now.getYear());
                LocalDateTime endTime = cafe.getEndTime().withDayOfMonth(now.getDayOfMonth()).withMonth(now.getMonthValue()).withYear(now.getYear());

                if (reservedListByTabldIdWithPagination.get(table.getId()) == null ) {
                    times.add(new availableTime(startTime, endTime));
                    tableReserveResDto.setAvailableTimeList(times);
                    cafeDetailReserveDto.addTable(tableReserveResDto);
                    continue;
                }

                for (Reserved reserved : reservedListByTabldIdWithPagination.get(table.getId())) {
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
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomDBException("해당 되는 카페가 없습니다.");
        }

    }

}
