package com.example.jariBean.controller;


import com.example.jariBean.config.auth.LoginUser;
import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.notice.NoticeResDto;
import com.example.jariBean.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NoticeController {

    private NoticeService noticeService;

    @GetMapping
    public ResponseEntity updateAlarmStatus(@AuthenticationPrincipal LoginUser loginUser) {
        List<NoticeResDto.NoticeSummaryResDto> noticeList = noticeService.findNoticeList();
        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", noticeList), CREATED);
    }
}
