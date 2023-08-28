package com.example.jariBean.controller;

import com.example.jariBean.config.auth.LoginUser;
import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.cafe.CafeResDto.CafeSummaryDto;
import com.example.jariBean.dto.reserved.ReservedResDto.ReserveSummaryResDto;
import com.example.jariBean.service.CafeService;
import com.example.jariBean.service.ReserveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {

    @Autowired
    private CafeService cafeService;

    @Autowired
    private ReserveService reserveService;

    @Operation(summary = "find reserve list", description = "api for find reserve list")
    @ApiResponse(
            responseCode = "200",
            description = "예약 내역 조회 성공",
            content = @Content(schema = @Schema(implementation = ReserveSummaryResDto.class))
    )
    @GetMapping("/reserve")
    public ResponseEntity homeReserve(@AuthenticationPrincipal LoginUser loginUser) {
        ReserveSummaryResDto reserveSummaryResDto = reserveService.getNearestReserved(loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "정보를 성공적으로 가져왔습니다.", reserveSummaryResDto), OK);
    }

    @Operation(summary = "find cafe list", description = "api for find cafe list")
    @ApiResponse(
            responseCode = "200",
            description = "예약 내역 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CafeSummaryDto.class)))
    )
    @GetMapping("/best")
    public ResponseEntity homeBest(Pageable pageable) {
        List<CafeSummaryDto> cafeSummaryDtos = cafeService.getCafeByMatchingCount(pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "정보를 성공적으로 가져왔습니다.", cafeSummaryDtos), OK);
    }
}
