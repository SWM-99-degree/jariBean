package com.example.jariBean.repository.user;

import com.example.jariBean.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import static com.example.jariBean.entity.User.UserRole.CUSTOMER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataMongoTest
class UserRepositoryTest {

    @Autowired UserRepository userRepository;

    @Test
    public void saveUser() throws Exception {
        // given
        String socialId = "kakao_1234";
        String nickname = "기무르따리";

        User user = User.builder()
                .socialId(socialId)
                .nickname(nickname)
                .role(CUSTOMER)
                .build();

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(user.getSocialId()).isEqualTo(savedUser.getSocialId());
        assertThat(user.getNickname()).isEqualTo(savedUser.getNickname());
        assertThat(user.getRole()).isEqualTo(savedUser.getRole());
    }

}