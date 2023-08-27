package com.example.jariBean.controller;

import com.example.jariBean.config.auth.LoginUser;
import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.user.UserResDto.UserInfoRespDto;
import com.example.jariBean.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "find user me", description = "api for find user me")
    @ApiResponse(
            responseCode = "200",
            description = "사용자 조회 성공",
            content = @Content(schema = @Schema(implementation = UserInfoRespDto.class))
    )
    @GetMapping("/me")
    public ResponseEntity findProfile(@AuthenticationPrincipal LoginUser loginUser) {
        UserInfoRespDto userInfo = userService.findUserInfo(loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "회원정보 조회 성공", userInfo), OK);
    }

    @Operation(summary = "register user", description = "api for register user")
    @ApiResponse(
            responseCode = "200",
            description = "사용자 회원가입 성공",
            content = @Content(schema = @Schema(implementation = UserInfoRespDto.class))
    )
    @PutMapping("/register")
    public ResponseEntity register(@AuthenticationPrincipal LoginUser loginUser) {
        UserInfoRespDto userInfo = userService.register(loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", userInfo), OK);
    }

}
