package com.example.jariBean.controller;

import com.example.jariBean.dto.cafe.CafeReqDto.CafeSearchReqDto;
import com.example.jariBean.dto.cafe.CafeResDto.CafeDetailReserveDto;
import com.example.jariBean.dto.cafe.CafeResDto.CafeSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cafe")
public class CafeController {

    @GetMapping("/best")
    public List<CafeSummaryDto> bestCafe() {
        return null;
    }

    @GetMapping("/{cafeId}")
    public CafeDetailReserveDto moreInfo(@PathVariable("cafeId") String cafeId) {
        return null;
    }

    @PostMapping
    public List<CafeSummaryDto> cafes(@RequestBody CafeSearchReqDto cafeSearchReqDto) {
        return null;
    }

}
