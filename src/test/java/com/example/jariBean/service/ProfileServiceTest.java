package com.example.jariBean.service;

import com.example.jariBean.dto.profile.ProfileReqDto;
import com.example.jariBean.dto.profile.ProfileResDto;
import com.example.jariBean.entity.User;
import com.example.jariBean.repository.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProfileServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findProfileTest(){
        // given
        String userId = "64d1082828032028b33c4450";

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
        String userId = "64d1d3992001592a8161cc89";

        // when
        userService.updateUserInfo(userId, profileUpdateReqDto);

        // then
        Assertions.assertEquals(userRepository.findById(userId).orElseThrow().getDescription(), "자고싶다");
    }

    @Test
    public void updateAlarmStatusTest(){
        // given
        User user = userRepository.findById("64d1082828032028b33c4450").orElseThrow();
        boolean bool = user.isAlarm();

        // when
        userService.updateAlarmStatus("64d1082828032028b33c4450");

        // then
        Assertions.assertEquals(userRepository.findById("64d1082828032028b33c4450").orElseThrow().isAlarm(), !bool);
    }
}
