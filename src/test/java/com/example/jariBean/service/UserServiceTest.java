package com.example.jariBean.service;

import com.example.jariBean.handler.ex.CustomApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Test
    void testExtractProvider_ValidFormat() {
        String socialId = "kakao_12345";
        String provider = userService.extractProvider(socialId);
        assertEquals("kakao", provider);
    }

    @Test
    void testExtractProvider_InvalidFormat() {
        String socialId = "invalid";
        CustomApiException exception = assertThrows(CustomApiException.class, () -> {
            userService.extractProvider(socialId);
        });
        assertEquals("잘못된 socialId 형식입니다.", exception.getMessage());
    }

    @Test
    void testExtractProvider_NullSocialId() {
        String socialId = null;
        CustomApiException exception = assertThrows(CustomApiException.class, () -> {
            userService.extractProvider(socialId);
        });
        assertEquals("사용자의 socialID의 값이 존재하지 않습니다.", exception.getMessage());
    }

}