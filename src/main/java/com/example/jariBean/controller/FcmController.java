package com.example.jariBean.controller;

import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.fcm.FCMReqDto.FCMTokenReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class FcmController {

    @PutMapping("/token")
    public ResponseEntity storingFCMTokenInRedis(@RequestBody FCMTokenReqDto fcmTokenReqDto) {
        return new ResponseEntity<>(new ResponseDto<>(1, "정보를 성공적으로 가져왔습니다.", null), OK);
    }

}
