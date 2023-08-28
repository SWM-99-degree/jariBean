package com.example.jariBean.service;

import com.example.jariBean.dto.profile.ProfileReqDto;
import com.example.jariBean.dto.profile.ProfileResDto;
import com.example.jariBean.entity.User;
import com.example.jariBean.repository.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.example.jariBean.entity.Role.CUSTOMER;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class ProfileServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

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

    @Test
    public void findProfileTest(){
        // given
        String userId = userRepository.findBySocialId("kakao_1234").orElseThrow().getId();

        // when
        ProfileResDto.ProfileSummaryResDto profileSummaryResDto = userService.findProfile(userId);

        // then
        Assertions.assertEquals(profileSummaryResDto.getNickName(), "기무르따리");
    }

    @Test
    public void updateUserInfo(){
        // given
        ProfileReqDto.ProfileUpdateReqDto profileUpdateReqDto = new ProfileReqDto.ProfileUpdateReqDto();
        profileUpdateReqDto.setDescription("자고싶다");
        String userId = userRepository.findBySocialId("kakao_1234").orElseThrow().getId();


        // when
        userService.updateUserInfo(userId, profileUpdateReqDto);

        // then
        Assertions.assertEquals(userRepository.findById(userId).orElseThrow().getDescription(), "자고싶다");
    }

    @Test
    public void updateAlarmStatusTest(){
        // given
        User user = userRepository.findBySocialId("kakao_1234").orElseThrow();
        boolean bool = user.isAlarm();

        // when
        userService.updateAlarmStatus(user.getId());

        // then
        Assertions.assertEquals(userRepository.findById(user.getId()).orElseThrow().isAlarm(), !bool);
    }
}
