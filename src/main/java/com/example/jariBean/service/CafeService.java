package com.example.jariBean.service;

import com.example.jariBean.dto.cafe.CafeResDto.CafeDetailDto;
import com.example.jariBean.dto.cafe.CafeResDto.CafeDetailReserveDto;
import com.example.jariBean.dto.cafe.CafeResDto.CafeSummaryDto;
import com.example.jariBean.dto.reserved.ReservedResDto.TableReserveResDto;
import com.example.jariBean.dto.reserved.ReservedResDto.availableTime;
import com.example.jariBean.dto.table.TableResDto.TableDetailDto;
import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.CafeOperatingTime;
import com.example.jariBean.entity.Reserved;
import com.example.jariBean.entity.TableClass;
import com.example.jariBean.handler.ex.CustomDBException;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.cafeOperatingTime.CafeOperatingTimeRepository;
import com.example.jariBean.repository.matching.MatchingRepository;
import com.example.jariBean.repository.reserved.ReservedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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

    private final CafeOperatingTimeRepository cafeOperatingTimeRepository;


    public List<CafeSummaryDto> getCafeByMatchingCount(Pageable pageable){
        try {
            List<String> cafeList = matchingRepository.findCafeIdSortedByCount(pageable);
            List<CafeSummaryDto> cafeSummaryDtos = new ArrayList<>();
            cafeRepository.findByIds(cafeList).forEach(cafe -> cafeSummaryDtos.add(new CafeSummaryDto(cafe)));
            return cafeSummaryDtos;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomDBException("DB에 조회하신 정보가 없습니다.");
        }

    }

    public CafeDetailReserveDto getCafeWithSearchingReserved(String cafeId, LocalDateTime reserveStartTime, LocalDateTime reserveEndTime, Integer peopleNumber, List<TableClass.TableOption> tableOptions) {

        CafeDetailReserveDto cafeDetailReserveDto = new CafeDetailReserveDto();
        try {
            Cafe cafe = cafeRepository.findById(cafeId).orElseThrow();
            CafeOperatingTime cafeOperatingTime = cafeOperatingTimeRepository.findById(cafeId).orElseThrow();
            cafeDetailReserveDto.setCafeDetailDto(new CafeDetailDto(cafe, cafeOperatingTime));

            Map<String, List<Reserved>> reservedListByTabldId = new HashMap<>();
            reservedRepository.findReservedByConditions(cafeId, reserveStartTime, reserveEndTime, peopleNumber, tableOptions).forEach(reserved ->
                    {
                        if (!reservedListByTabldId.containsKey(reserved.getTable().getId())){
                            reservedListByTabldId.put(reserved.getTable().getId(), new ArrayList<>());
                        }
                        reservedListByTabldId.get(reserved.getTable().getId()).add(reserved);
                    }
            );

            for (Map.Entry<String, List<Reserved>> reservedList :reservedListByTabldId.entrySet()){
                TableReserveResDto tableReserveResDto = new TableReserveResDto();
                TableDetailDto tableDetailDto = new TableDetailDto(reservedList.getValue().get(0).getTable());
                tableReserveResDto.setTableDetailDto(tableDetailDto);

                List<availableTime> times = new ArrayList<>();
                LocalDateTime startTime = reserveStartTime;
                LocalDateTime endTime = reserveEndTime;
                for (Reserved reserved : reservedList.getValue()) {
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


    public CafeDetailReserveDto getCafeWithTodayReserved(String cafeId){

        CafeDetailReserveDto cafeDetailReserveDto = new CafeDetailReserveDto();
        LocalDateTime now = LocalDateTime.now();
        try {
            Cafe cafe = cafeRepository.findById(cafeId).orElseThrow();
            CafeOperatingTime cafeOperatingTime = cafeOperatingTimeRepository.findById(cafeId).orElseThrow();
            cafeDetailReserveDto.setCafeDetailDto(new CafeDetailDto(cafe, cafeOperatingTime));


            Map<String, List<Reserved>> reservedListByTabldId = new HashMap<>();
            reservedRepository.findTodayReservedById(cafeId).forEach(reserved ->
                    {
                        if (!reservedListByTabldId.containsKey(reserved.getTable().getId())){
                            reservedListByTabldId.put(reserved.getTable().getId(), new ArrayList<>());
                        }
                        reservedListByTabldId.get(reserved.getTable().getId()).add(reserved);
                    }
            );

            for (Map.Entry<String, List<Reserved>> reservedList :reservedListByTabldId.entrySet()){
                TableReserveResDto tableReserveResDto = new TableReserveResDto();
                TableDetailDto tableDetailDto = new TableDetailDto(reservedList.getValue().get(0).getTable());
                tableReserveResDto.setTableDetailDto(tableDetailDto);

                List<availableTime> times = new ArrayList<>();
                LocalDateTime startTime = cafeOperatingTime.getOpenTime().withDayOfMonth(now.getDayOfMonth()).withMonth(now.getMonthValue()).withYear(now.getYear());
                LocalDateTime endTime = cafeOperatingTime.getCloseTime().withDayOfMonth(now.getDayOfMonth()).withMonth(now.getMonthValue()).withYear(now.getYear());
                for (Reserved reserved : reservedList.getValue()) {
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
            throw new CustomDBException("해당 되는 카페가 없습니다.");
        }

    }

}
