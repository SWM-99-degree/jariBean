package com.example.jariBean.service;

import com.example.jariBean.dto.user.UserReqDto.UserJoinReqDto;
import com.example.jariBean.dto.user.UserResDto.UserJoinRespDto;
import com.example.jariBean.entity.User;
import com.example.jariBean.handler.ex.CustomApiException;
import com.example.jariBean.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public UserJoinRespDto save(UserJoinReqDto joinReqDto) {
        // userPhoneNumber 중복 검사
        if(userRepository.existsBySocialId(joinReqDto.getUserPhoneNumber())) {
            throw new CustomApiException("동일한 userPhoneNumber 존재합니다.");
        }
        // 회원가입
        User savedUser = userRepository.save(joinReqDto.toEntity(passwordEncoder));
        // 회원가입 결과 반환
        return new UserJoinRespDto(savedUser);
    }


}
