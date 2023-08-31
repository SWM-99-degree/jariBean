package com.example.jariBean.controller;


import com.example.jariBean.config.auth.LoginUser;
import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.notice.NoticeResDto.NoticeSummaryResDto;
import com.example.jariBean.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
@RequestMapping("/api/notification")
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "find notice list", description = "api for find notice list")
    @ApiResponse(
            responseCode = "200",
            description = "공지 내역 조회",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = NoticeSummaryResDto.class)))
    )
    @GetMapping
    public ResponseEntity getNoticeList(@AuthenticationPrincipal LoginUser loginUser, Pageable pageable) {
        Page<NoticeSummaryResDto> noticeList = noticeService.findNoticeList(pageable);
        return new ResponseEntity<>(new ResponseDto<>(1, "Noice를 불러왔습니다.", noticeList), OK);
    }

}
