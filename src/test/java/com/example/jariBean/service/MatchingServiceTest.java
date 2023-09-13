package com.example.jariBean.service;

import com.example.jariBean.dto.cafe.CafeResDto;
import com.example.jariBean.dto.matching.MatchingResDto;
import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.Matching;
import com.example.jariBean.repository.cafe.CafeRepository;
import com.example.jariBean.repository.matching.MatchingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class MatchingServiceTest {

    @Autowired
    private CafeRepository cafeRepository;

    @Autowired private MatchingRepository matchingRepository;

    @Autowired private MatchingService matchingService;

    @Autowired
    private CafeService cafeService;



    @Test
    public void matchingTest() {
        // given
        cafeRepository.deleteAll();
        matchingRepository.deleteAll();
        String userId = "64d1082828032028b33c4450";
        Cafe cafe = cafeRepository.save(new Cafe());
        Integer number = 2;
        Matching matching = new Matching(userId, cafe, number, "기요옹");
        matchingRepository.save(matching);

        // when
        Page<MatchingResDto.MatchingSummaryResDto> matchingSummaryResDtopList = matchingService.findMatchingByUserId("64d1082828032028b33c4450", Pageable.ofSize(1));

        // then
        for (MatchingResDto.MatchingSummaryResDto matchingSummaryResDto:matchingSummaryResDtopList){
            Assertions.assertEquals(matchingSummaryResDto.getSeating(), number);
            Assertions.assertEquals(matchingSummaryResDto.getCafeSummaryDto().getId(), cafe.getId());
            break;
        }

    }

    @Test
    public void progressingTest() {
        // given
        Cafe cafe = cafeRepository.save(new Cafe());

        Matching matching1 = new Matching("testUser1", cafe, 3, "철수");
        Matching matching2 = new Matching("testUser2", cafe, 3, "얌얌");
        Matching matching3 = new Matching("testUser3", cafe, 3, "영희");

        matchingRepository.save(matching1);
        matchingRepository.save(matching2);
        matchingRepository.save(matching3);

        // when
        List<MatchingResDto.MatchingSummaryResForCafeDto> summaryResForCafeDtoList = matchingService.findMatchingByCafe(cafe.getId());

        //then
        Assertions.assertEquals(summaryResForCafeDtoList.size(), 3);

        cafeRepository.deleteAll();
        matchingRepository.deleteAll();
    }

    @Test
    public void bestCafesTest() {
        // when
        Page<CafeResDto.CafeSummaryDto> matchingSummaryResDtoList = cafeService.getCafeByMatchingCount(Pageable.ofSize(1));
    }
}
