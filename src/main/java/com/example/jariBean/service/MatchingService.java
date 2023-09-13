package com.example.jariBean.service;

import com.example.jariBean.dto.matching.MatchingResDto;
import com.example.jariBean.dto.matching.MatchingResDto.MatchingSummaryResDto;
import com.example.jariBean.entity.Matching;
import com.example.jariBean.handler.ex.CustomDBException;
import com.example.jariBean.repository.matching.MatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchingService {

    @Autowired
    private MatchingRepository matchingRepository;

    public Page<MatchingSummaryResDto> findMatchingByUserId(String userId, Pageable pageable) {

        Page<Matching> page = matchingRepository.findByUserId(userId, pageable);
        Page<MatchingSummaryResDto> matchingResDtos = page.map(matching -> new MatchingSummaryResDto(matching));

        return matchingResDtos;
    }

    public List<MatchingResDto.MatchingSummaryResForCafeDto> findMatchingByCafe(String cafeId) {
        List<Matching> matchingList = matchingRepository.findMatchingProgress(cafeId).orElseThrow(() -> new CustomDBException("Matching DB에 문제가 있습니다."));
        List<MatchingResDto.MatchingSummaryResForCafeDto> matchingResDtos = matchingList.stream()
                .map(matching -> new MatchingResDto.MatchingSummaryResForCafeDto(matching))
                .collect(Collectors.toList());
        return matchingResDtos;
    }

}
