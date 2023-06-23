package com.example.jariBean.controller;

import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.cafe.CafeReqDto;
import com.example.jariBean.dto.cafe.CafeResDto;
import com.example.jariBean.service.CafeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cafes")
public class CafeController {

    private final CafeService cafeService;

    @Operation(summary = "join user", description = "api for join user")
    @PostMapping("/join")
    public ResponseEntity join(@RequestBody @Valid CafeReqDto.CafeJoinReqDto joinReqDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errorMap.put(error.getField(), error.getDefaultMessage());
            });
            return new ResponseEntity<>(new ResponseDto<>(-1, "유효성 검사 실패", errorMap), BAD_REQUEST);
        }

        CafeResDto.CafeJoinRespDto savedCafe = cafeService.save(joinReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", savedCafe), CREATED);
    }
}
