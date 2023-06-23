package com.example.jariBean.service;

import com.example.jariBean.dto.cafe.CafeReqDto;
import com.example.jariBean.dto.cafe.CafeResDto;
import com.example.jariBean.entity.Cafe;
import com.example.jariBean.handler.ex.CustomApiException;
import com.example.jariBean.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CafeService {

    private final PasswordEncoder passwordEncoder;
    private final CafeRepository cafeRepository;

    //
    @Transactional
    public CafeResDto.CafeJoinRespDto save(CafeReqDto.CafeJoinReqDto cafeJoinReqDto) {
        // userPhoneNumber 중복 검사
        if(cafeRepository.existsByCafePhoneNumber(cafeJoinReqDto.getCafePhoneNumber())) {
            throw new CustomApiException("동일한 cafePhoneNumber 존재합니다.");
        }
        // 회원가입
        Cafe savedCafe = cafeRepository.save(cafeJoinReqDto.toEntity(passwordEncoder));
        // 회원가입 결과 반환
        return new CafeResDto.CafeJoinRespDto(savedCafe);
    }
}
