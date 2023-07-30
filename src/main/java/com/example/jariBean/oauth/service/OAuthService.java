package com.example.jariBean.oauth.service;

import com.example.jariBean.entity.User;
import com.example.jariBean.oauth.service.OAuthKakaoService.SocialUserInfo;
import com.example.jariBean.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.jariBean.entity.User.UserRole.CUSTOMER;

@Service
@RequiredArgsConstructor
public abstract class OAuthService {

    private final UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public abstract String getAccessToken(String code);

    public abstract SocialUserInfo getUserInfo(String accessToken);

    public User saveOrUpdate(SocialUserInfo socialUserInfo) {
        User user = userRepository.findBySocialId(socialUserInfo.getSocialId())
                .orElse(User.builder()
                        .socialId(socialUserInfo.getSocialId())
                        .password(passwordEncoder.encode(socialUserInfo.getNickname()))
                        .role(CUSTOMER)
                        .build());

        user.updateInfo(socialUserInfo.getNickname(), socialUserInfo.getImageUrl());
        return userRepository.save(user);
    }
}