package com.example.jariBean.service;

import com.example.jariBean.dto.profile.ProfileReqDto;
import com.example.jariBean.dto.profile.ProfileResDto.ProfileSummaryResDto;
import com.example.jariBean.dto.user.UserReqDto.UserJoinReqDto;
import com.example.jariBean.dto.user.UserResDto.UserInfoRespDto;
import com.example.jariBean.dto.user.UserResDto.UserJoinRespDto;
import com.example.jariBean.entity.User;
import com.example.jariBean.handler.ex.CustomApiException;
import com.example.jariBean.handler.ex.CustomDBException;
import com.example.jariBean.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ProfileSummaryResDto findProfile(String userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow();
            return new ProfileSummaryResDto(user);
        } catch (Exception e) {
            throw new CustomDBException("유저가 존재하지 않습니다.");
        }
    }

    @Transactional
    public void updateUserInfo(String userId, ProfileReqDto.ProfileUpdateReqDto profileUpdateReqDto){
        try {
            User user = userRepository.findById(userId).orElseThrow();
            user.updateInfo(profileUpdateReqDto.getNickname(), profileUpdateReqDto.getImageUrl(), profileUpdateReqDto.getDescription());
            userRepository.save(user);
        } catch (Exception e) {
            throw new CustomDBException("유저가 존재하지 않습니다.");
        }
    }

    @Transactional
    public void updateAlarmStatus(String userId){
        try {
            User user = userRepository.findById(userId).orElseThrow();
            user.updateAlarm();
            userRepository.save(user);
        } catch (Exception e) {
            throw new CustomDBException("유저 DB에 오류가 존재합니다.");
        }
    }

    public UserInfoRespDto findUserInfo(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomDBException("no user exists for the given id"));
        return UserInfoRespDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .imageUrl(user.getImage())
                .description(user.getDescription())
                .socialId(extractProvider(user.getSocialId()))
                .role(user.getRole())
                .build();
    }

    @Transactional
    public UserInfoRespDto register(String userId) {
        // find user by id
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomDBException("no user exists for the given id"));

        // update user role
        user.register();
        userRepository.save(user);

        // return user info
        return UserInfoRespDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .imageUrl(user.getImage())
                .description(user.getDescription())
                .role(user.getRole())
                .build();
    }

    public String extractProvider(String socialId) {
        // socialId의 값이 null인 경우
        if (socialId == null) {
            throw new CustomApiException("사용자의 socialID의 값이 존재하지 않습니다.");
        }

        int underscoreIndex = socialId.indexOf('_');

        // socialId의 형식이 잘못 된 경우
        if (underscoreIndex == -1 || underscoreIndex >= socialId.length() - 1) {
            throw new CustomApiException("잘못된 socialId 형식입니다.");
        }

        return socialId.substring(0, underscoreIndex);
    }
}
