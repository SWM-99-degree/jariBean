package com.example.jariBean.service;

import com.example.jariBean.dto.matching.MatchingResDto.MatchingSummaryResDto;
import com.example.jariBean.entity.Matching;
import com.example.jariBean.handler.ex.CustomDBException;
import com.example.jariBean.repository.matching.MatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchingService {
    private MatchingRepository matchingRepository;

    public List<MatchingSummaryResDto> findMatchingByUserId(String userId, Pageable pageable) {
        List<MatchingSummaryResDto> matchingResDtoList = new ArrayList<>();
        try {
            List<Matching> matchingList = matchingRepository.findByUserId(userId, pageable);
            matchingList.forEach(matching -> matchingResDtoList.add(new MatchingSummaryResDto(matching)));
            return matchingResDtoList;
        } catch (Exception e) {
            throw new CustomDBException("예약 관련 데이터에 문제가 있습니다.");
        }
    }
}