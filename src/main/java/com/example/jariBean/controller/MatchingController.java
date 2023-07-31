package com.example.jariBean.controller;

import com.example.jariBean.dto.matching.MatchingResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matching")
public class MatchingController {

    @GetMapping
    public List<MatchingResDto> matchingList() {
        return null;
    }
}
