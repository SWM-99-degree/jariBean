package com.example.jariBean.controller;


import com.example.jariBean.config.auth.LoginUser;
import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.profile.ProfileReqDto.ProfileUpdateReqDto;
import com.example.jariBean.dto.profile.ProfileResDto.ProfileSummaryResDto;
import com.example.jariBean.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles")
public class ProfileController {

    private UserService userService;


    @PutMapping("/alarm")
    public ResponseEntity updateAlarmStatus(@AuthenticationPrincipal LoginUser loginUser) {
        userService.updateAlarmStatus(loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", ""), CREATED);
    }

    @GetMapping
    public ResponseEntity getProfile(@AuthenticationPrincipal LoginUser loginUser) {
        ProfileSummaryResDto profileSummaryResDto = userService.findProfile(loginUser.getUser().getId());
        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", profileSummaryResDto), CREATED);
    }

    @PutMapping
    public ResponseEntity updateProfile(@AuthenticationPrincipal LoginUser loginUser, @Validated @RequestBody ProfileUpdateReqDto profileUpdateReqDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errorMap.put(error.getField(), error.getDefaultMessage());
            });
            return new ResponseEntity<>(new ResponseDto<>(-1, "유효성 검사 실패", errorMap), BAD_REQUEST);
        }

        userService.updateUserInfo(loginUser.getUser().getId(), profileUpdateReqDto);

        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", ""), CREATED);
    }




}
