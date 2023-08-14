package com.example.jariBean.controller;

import com.example.jariBean.config.auth.LoginUser;
import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.cafe.CafeResDto;
import com.example.jariBean.dto.home.HomeResDto;
import com.example.jariBean.dto.reserved.ReserveReqDto;
import com.example.jariBean.dto.reserved.ReservedResDto;
import com.example.jariBean.service.CafeService;
import com.example.jariBean.service.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {

    @Autowired
    private CafeService cafeService;

    @Autowired
    private ReserveService reserveService;

    @GetMapping("/reserve")
    public ResponseEntity homeReserve(@AuthenticationPrincipal LoginUser loginUser) {
        ReservedResDto.ReserveSummaryResDto reserveSummaryResDto = reserveService.getNearestReserved(loginUser.getUser().getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "정보를 성공적으로 가져왔습니다.", reserveSummaryResDto), CREATED);
    }

    @GetMapping("/best")
    public ResponseEntity homeBest(Pageable pageable) {
        List<CafeResDto.CafeSummaryDto> cafeSummaryDtos = cafeService.getCafeByMatchingCount(pageable);

        return new ResponseEntity<>(new ResponseDto<>(1, "정보를 성공적으로 가져왔습니다.", cafeSummaryDtos), CREATED);
    }
}
