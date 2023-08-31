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
}
