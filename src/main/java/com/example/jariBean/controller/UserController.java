package com.example.jariBean.controller;

import com.example.jariBean.config.auth.LoginUser;
import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.user.UserReqDto.UserRegisterReqDto;
import com.example.jariBean.dto.user.UserResDto.UserInfoRespDto;
import com.example.jariBean.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "find user me", description = "api for find user me")
    @GetMapping("/me")
    public ResponseEntity findProfile(@AuthenticationPrincipal LoginUser loginUser) {
        UserInfoRespDto userInfo = userService.findUserInfo(loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "회원정보 조회 성공", userInfo), OK);
    }

    @Operation(summary = "register user", description = "api for register user")
    @PutMapping("/register")
    public ResponseEntity register(@RequestBody UserRegisterReqDto userReqDto) {
        UserInfoRespDto userInfo = userService.register(userReqDto);

        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", userInfo), OK);
    }

}
