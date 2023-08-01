package com.example.jariBean.service;

import com.example.jariBean.dto.cafe.CafeResDto.CafeDetailReserveDto;
import com.example.jariBean.dto.cafe.CafeResDto.CafeSummaryDto;
import com.example.jariBean.dto.cafe.CafeResDto.CafeDetailDto;
import com.example.jariBean.dto.reserved.ReservedResDto.TableReserveResDto;
import com.example.jariBean.dto.table.TableResDto.TableDetailDto;
import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.CafeOperatingTime;
import com.example.jariBean.entity.Reserved;
import com.example.jariBean.handler.ex.CustomDBException;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.cafeOperatingTime.CafeOperatingTimeRepository;
import com.example.jariBean.repository.matching.MatchingRepository;
import com.example.jariBean.repository.reserved.ReservedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final CafeOperatingTimeRepository cafeOperatingTimeRepository;


    public List<CafeSummaryDto> getCafeByMatchingCount(){
        try {
            List<String> cafeList = matchingRepository.findCafeIdSortedByCount();
            List<CafeSummaryDto> cafeSummaryDtos = new ArrayList<>();
            cafeRepository.findByIds(cafeList).forEach(cafe -> cafeSummaryDtos.add(new CafeSummaryDto(cafe)));
            return cafeSummaryDtos;
        } catch (Exception e) {
            throw new CustomDBException("DB에 조회하신 정보가 없습니다.");
        }

    }

    // TODO 완성 필요
    public CafeDetailReserveDto getCafeWithTodayReserved(String cafeId){
//        CafeDetailReserveDto cafeDetailReserveDto = new CafeDetailReserveDto();
//        LocalDateTime time = LocalDateTime.now();
//        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow();
//        CafeOperatingTime cafeOperatingTime = cafeOperatingTimeRepository.findById(cafeId).orElseThrow();
//
//        Map<String, List<Reserved>> reservedListByTabldId = new HashMap<>();
//        reservedRepository.findTodayReservedById(cafeId).forEach(reserved ->
//                {
//                    if (!reservedListByTabldId.containsKey(reserved.getTable().getId())){
//                        reservedListByTabldId.put(reserved.getTable().getId(), new ArrayList<>());
//                    }
//                    reservedListByTabldId.get(reserved.getTable().getId()).add(reserved);
//                }
//        );
//
//        for (Map.Entry<String, List<Reserved>> reservedList :reservedListByTabldId.entrySet()){
//            TableReserveResDto tableReserveResDto = new TableReserveResDto();
//            TableDetailDto tableDetailDto = new TableDetailDto(reservedList.getValue());
//
//
//
//
//        }
//
//
//
//
//

        return null;

    }


}
