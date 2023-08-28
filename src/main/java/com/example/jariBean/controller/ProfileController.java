package com.example.jariBean.controller;


import com.example.jariBean.config.auth.LoginUser;
import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.profile.ProfileReqDto.ProfileUpdateReqDto;
import com.example.jariBean.dto.profile.ProfileResDto.ProfileSummaryResDto;
import com.example.jariBean.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileController {

    private UserService userService;

    @Operation(summary = "change alarm status", description = "api for change alarm status")
    @ApiResponse(
            responseCode = "200",
            description = "알람 상태 변경 완료",
            content = @Content(schema = @Schema(implementation = Void.class))
    )
    @PutMapping("/alarm")
    public ResponseEntity updateAlarmStatus(@AuthenticationPrincipal LoginUser loginUser) {
        userService.updateAlarmStatus(loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "알람 상태 변경", null), OK);
    }

    @Operation(summary = "find profile status", description = "api for find profile status")
    @ApiResponse(
            responseCode = "200",
            description = "개인 프로필 정보 조회 성공",
            content = @Content(schema = @Schema(implementation = ProfileSummaryResDto.class))
    )
    @GetMapping
    public ResponseEntity getProfile(@AuthenticationPrincipal LoginUser loginUser) {
        ProfileSummaryResDto profileSummaryResDto = userService.findProfile(loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "개인 프로필 정보 조회 성공", profileSummaryResDto), OK);
    }

    @Operation(summary = "update profile information", description = "api for update profile information")
    @ApiResponse(
            responseCode = "200",
            description = "개인 프로필 정보 수정 성공",
            content = @Content(schema = @Schema(implementation = Void.class))
    )
    @PutMapping
    public ResponseEntity updateProfile(@AuthenticationPrincipal LoginUser loginUser, @Valid @RequestBody ProfileUpdateReqDto profileUpdateReqDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errorMap.put(error.getField(), error.getDefaultMessage());
            });
            return new ResponseEntity<>(new ResponseDto<>(-1, "유효성 검사 실패", errorMap), BAD_REQUEST);
        }
        userService.updateUserInfo(loginUser.getUser().getId(), profileUpdateReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", null), OK);
    }




}
