package com.example.jariBean.oauth.service;

import com.example.jariBean.config.jwt.JwtProcess;
import com.example.jariBean.entity.User;
import com.example.jariBean.oauth.dto.LoginResDto.LoginSuccessResDto;
import com.example.jariBean.oauth.service.OAuthKakaoService.SocialUserInfo;
import com.example.jariBean.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.jariBean.entity.User.UserRole.UNREGISTERED;

@Service
@RequiredArgsConstructor
public abstract class OAuthService {

    private final UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public abstract String getAccessToken(String code);

    public abstract SocialUserInfo getUserInfo(String accessToken);

    public LoginSuccessResDto saveOrUpdate(SocialUserInfo socialUserInfo, String registrationId) {

        // save or create user
        User user = userRepository.findBySocialId(socialUserInfo.getSocialId())
                .orElse(User.builder()
                        .socialId(socialUserInfo.getSocialId())
                        .password(passwordEncoder.encode(socialUserInfo.getNickname()))
                        .role(UNREGISTERED)
                        .build());

        // update user info
        user.updateBySocialInfo(socialUserInfo.getNickname(), socialUserInfo.getImageUrl(), passwordEncoder.encode(socialUserInfo.getNickname()));

        // save or update user
        User savedUser = userRepository.save(user);

        return LoginSuccessResDto.builder()
                .accessToken(JwtProcess.create(savedUser))
                .refreshToken(JwtProcess.createRefreshToken(savedUser))
                .build();
    }

    public boolean isExistUser(String socialId) {
        return userRepository.existsBySocialId(socialId);
    }
}