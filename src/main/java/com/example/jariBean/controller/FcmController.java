package com.example.jariBean.controller;

import com.auth0.jwt.JWT;
import com.example.jariBean.config.auth.LoginCafe;
import com.example.jariBean.config.auth.LoginUser;
import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.fcm.FCMReqDto.FCMTokenReqDto;
import com.example.jariBean.service.FcmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class FcmController {

    private final FcmService fcmService;
    @Operation(summary = "store fcm token", description = "api for store fcm token")
    @ApiResponse(
            responseCode = "201",
            description = "예약 내역 저장 성공"
    )
    @PutMapping("/token")
    public ResponseEntity storingFCMTokenInRedis(@AuthenticationPrincipal UserDetails loginUser,
                                                 @RequestBody FCMTokenReqDto fcmTokenReqDto, HttpServletRequest httpServletRequest) {
        String role = JWT.decode(httpServletRequest.getHeader("ACCESS_AUTHORIZATION")).getClaim("userRole").toString();
        if (role.equals("\"MANAGER\"")) {
            LoginCafe cafe = (LoginCafe) loginUser;
            fcmService.updateFirebaseToken(cafe.getCafeManager().getId(), fcmTokenReqDto);
        } else {
            LoginUser user = (LoginUser) loginUser;
            fcmService.updateFirebaseToken(user.getUser().getId(), fcmTokenReqDto);
        }
        return new ResponseEntity<>(new ResponseDto<>(1, "FCM 토큰 정보 갱신에 성공하였습니다.", null), CREATED);
    }

}
