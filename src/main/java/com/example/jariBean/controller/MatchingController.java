package com.example.jariBean.controller;

import com.example.jariBean.config.auth.LoginUser;
import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.matching.MatchingResDto.MatchingSummaryResDto;
import com.example.jariBean.service.MatchingService;
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
@RequestMapping("/api/matching")
public class MatchingController {


    private final MatchingService matchingService;


    @Operation(summary = "find matching list", description = "api for find matching list")
    @ApiResponse(
            responseCode = "200",
            description = "매칭 내역 조회 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = MatchingSummaryResDto.class)))
    )
    @GetMapping
    public ResponseEntity matchingList(@AuthenticationPrincipal LoginUser loginUser, Pageable pageable) {
        String userId = loginUser.getUser().getId();
        Page<MatchingSummaryResDto> matchingSummaryResDtoList = matchingService.findMatchingByUserId(userId, pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "정보를 성공적으로 가져왔습니다", matchingSummaryResDtoList), OK);
    }
}
