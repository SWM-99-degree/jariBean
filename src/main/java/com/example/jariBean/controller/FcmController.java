package com.example.jariBean.controller;

import com.example.jariBean.config.auth.LoginUser;
import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.fcm.FCMReqDto.FCMTokenReqDto;
import com.example.jariBean.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class FcmController {

    private final FcmService fcmService;

    @PutMapping("/token")
    public ResponseEntity storingFCMTokenInRedis(@AuthenticationPrincipal LoginUser loginUser,
                                                 @RequestBody FCMTokenReqDto fcmTokenReqDto) {
        fcmService.updateFirebaseToken(loginUser.getUser().getId(), fcmTokenReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "FCM 토큰 정보 갱신에 성공하였습니다.", null), OK);
    }

}
