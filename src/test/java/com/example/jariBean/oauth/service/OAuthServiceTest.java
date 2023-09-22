package com.example.jariBean.oauth.service;

import com.example.jariBean.config.jwt.JwtProcess;
import com.example.jariBean.entity.User;
import com.example.jariBean.handler.ex.CustomApiException;
import com.example.jariBean.repository.TokenRepository;
import com.example.jariBean.repository.user.UserRepository;
import com.example.jariBean.service.oauth.OAuthKakaoService;
import com.example.jariBean.service.oauth.OAuthKakaoService.SocialUserInfo;
import com.example.jariBean.service.oauth.OAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class OAuthServiceTest {

    @Autowired UserRepository userRepository;
    @Autowired TokenRepository tokenRepository;
    @Autowired JwtProcess jwtProcess;
    OAuthService oAuthService;

    @BeforeEach
    public void beforeEach() {
        oAuthService = new OAuthKakaoService(userRepository, jwtProcess, tokenRepository);
    }

    @Test
    public void saveOrUpdateTest() throws Exception {
        // nickname is null
        SocialUserInfo kakaoSocialUserInfo1 = SocialUserInfo.create("KAKAO", "kakao_id", "Guest", "kakak0_imageUrl");
        oAuthService.saveOrUpdate(kakaoSocialUserInfo1);

        User beforeFindUser = userRepository.findBySocialId("KAKAO_kakao_id")
                .orElseThrow(() -> new CustomApiException("Could not find user information by social id"));
        assertThat(beforeFindUser.getNickname()).isEqualTo("Guest");

        // nickname is not null
        SocialUserInfo kakaoSocialUserInfo2 = SocialUserInfo.create("KAKAO", "kakao_id", "kakao_nickname", "kakak0_imageUrl");
        oAuthService.saveOrUpdate(kakaoSocialUserInfo2);

        User afterFindUser = userRepository.findBySocialId("KAKAO_kakao_id")
                .orElseThrow(() -> new CustomApiException("Could not find user information by social id"));
        assertThat(afterFindUser.getNickname()).isEqualTo("kakao_nickname");
    }

}