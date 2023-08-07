package com.example.jariBean.controller;

import com.example.jariBean.config.auth.LoginUser;
import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.reserved.ReserveReqDto.ReserveSaveReqDto;
import com.example.jariBean.dto.reserved.ReservedResDto.ReserveSummaryResDto;
import com.example.jariBean.service.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reserve")
public class ReserveController {

    private ReserveService reserveService;

    @GetMapping
    public ResponseEntity reserveList(@AuthenticationPrincipal LoginUser loginUser, Pageable pageable) {
        List<ReserveSummaryResDto> reserveSummaryResDtoList = reserveService.getMyReserved(loginUser.getUser().getId(), pageable);

        return new ResponseEntity<>(new ResponseDto<>(1, "정보를 성공적으로 가져왔습니다", reserveSummaryResDtoList), CREATED);
    }

    @PostMapping
    public ResponseEntity saveReserve(@AuthenticationPrincipal LoginUser loginUser, @RequestBody ReserveSaveReqDto reserveSaveReqDto) {

        reserveService.saveReserved(loginUser.getUser().getId(), reserveSaveReqDto);

        return new ResponseEntity<>(new ResponseDto<>(1, "정보가 성공적으로 등록되었습니다.", ""), CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteReserve(@PathVariable String id) {

        reserveService.deleteMyReserved(id);

        return new ResponseEntity<>(new ResponseDto<>(1, "정보가 성공적으로 삭제되었습니다.", ""), CREATED);
    }


}
