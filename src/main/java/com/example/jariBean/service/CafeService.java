package com.example.jariBean.service;

import com.example.jariBean.dto.cafe.CafeReqDto;
import com.example.jariBean.dto.cafe.CafeResDto;
import com.example.jariBean.entity.Cafe;
import com.example.jariBean.handler.ex.CustomApiException;
import com.example.jariBean.repository.cafe.CafeRepository;
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
    private final CafeRepository cafeRepository;

}
