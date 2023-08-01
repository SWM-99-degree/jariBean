package com.example.jariBean.controller;

import com.example.jariBean.dto.reserved.ReserveReqDto.ReserveSaveReqDto;
import com.example.jariBean.dto.reserved.ReservedResDto.ReserveSummaryResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reserve")
public class ReserveController {

    @GetMapping
    public List<ReserveSummaryResDto> reserveList() {
        return null;
    }

    @PostMapping
    public void saveReserve(@RequestBody ReserveSaveReqDto reserveSaveReqDto) {
        return;
    }
}
