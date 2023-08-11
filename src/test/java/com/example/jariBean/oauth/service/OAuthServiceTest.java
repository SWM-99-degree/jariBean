package com.example.jariBean.oauth.service;

import com.example.jariBean.entity.User;
import com.example.jariBean.repository.user.UserRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class OAuthServiceTest {

    @Autowired UserRepository userRepository;

    @Test
    public void isExistUserTest() throws Exception {

        OAuthService oAuthService = new OAuthKakaoService(userRepository);

        // 회원가입을 하지 않은 유저
        boolean isNotLoggedIn = oAuthService.isExistUser("no-social-id");
        Assertions.assertThat(isNotLoggedIn).isEqualTo(false);

        // 회원가입된 유저
        User user = User.builder().socialId("social-id").password("password").build();
        userRepository.save(user);

        boolean isLoggedIn = oAuthService.isExistUser(user.getSocialId());
        Assertions.assertThat(isLoggedIn).isEqualTo(true);

    }

}