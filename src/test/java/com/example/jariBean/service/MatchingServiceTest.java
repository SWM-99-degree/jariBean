package com.example.jariBean.service;

import com.example.jariBean.dto.matching.MatchingResDto;
import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.Matching;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.matching.MatchingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@SpringBootTest
public class MatchingServiceTest {

    @Autowired
    private CafeRepository cafeRepository;

    @Autowired
    private MatchingRepository matchingRepository;

    @Autowired
    private MatchingService matchingService;


    @Test
    public void matchingTest() {
        // given
        String userId = "64d1082828032028b33c4450";
        Cafe cafe = cafeRepository.findById("64c45ac3935eb61c140793e7").orElseThrow();
        Integer number = 2;
        Matching matching = new Matching(userId, cafe, number);

        // when
        matchingRepository.save(matching);
        List<MatchingResDto.MatchingSummaryResDto> matchingSummaryResDtopList = matchingService.findMatchingByUserId("64d1082828032028b33c4450", Pageable.ofSize(1));

        // then
        for (MatchingResDto.MatchingSummaryResDto matchingSummaryResDto:matchingSummaryResDtopList){
            Assertions.assertEquals(matchingSummaryResDto.getSeating(), number);
            Assertions.assertEquals(matchingSummaryResDto.getCafeSummaryDto().getId(), cafe.getId());
            break;
        }

    }
}
