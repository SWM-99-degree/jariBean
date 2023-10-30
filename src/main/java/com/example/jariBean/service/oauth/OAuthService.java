package com.example.jariBean.service.oauth;

import com.example.jariBean.config.jwt.JwtProcess;
import com.example.jariBean.dto.oauth.LoginResDto.LoginSuccessResDto;
import com.example.jariBean.entity.Token;
import com.example.jariBean.entity.User;
import com.example.jariBean.repository.TokenRepository;
import com.example.jariBean.repository.user.UserRepository;
import com.example.jariBean.service.oauth.OAuthKakaoService.SocialUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.jariBean.config.jwt.JwtProcess.TokenType.ACCESS;
import static com.example.jariBean.config.jwt.JwtProcess.TokenType.REFRESH;
import static com.example.jariBean.entity.Role.UNREGISTERED;

@Service
@RequiredArgsConstructor
public abstract class OAuthService {

    protected final UserRepository userRepository;
    protected final TokenRepository tokenRepository;
    private final JwtProcess jwtProcess;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public abstract String getAccessToken(String code);

    public abstract SocialUserInfo getUserInfo(String accessToken);

    public LoginSuccessResDto saveOrUpdate(SocialUserInfo socialUserInfo) {

        // save or update user
        User savedUser = saveOrUpdateUser(socialUserInfo);

        //create JWT
        String accessToken = jwtProcess.createJWT(savedUser.getId(), savedUser.getRole().toString(), savedUser.getNickname(), ACCESS);
        String refreshToken = jwtProcess.createJWT(savedUser.getId(), savedUser.getRole().toString(), savedUser.getNickname(), REFRESH);

        // storing jwt in redis
        saveToken(savedUser.getId(), accessToken, refreshToken);

        return LoginSuccessResDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveToken(String userId, String accessToken, String refreshToken) {
        Token token = Token.builder()
                .userId(userId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        tokenRepository.save(token);
    }

    private User saveOrUpdateUser(SocialUserInfo socialUserInfo) {
        // save or create user
        User user = userRepository.findBySocialId(socialUserInfo.getSocialId())
                .orElse(User.builder()
                        .socialId(socialUserInfo.getSocialId())
                        .nickname(socialUserInfo.getNickname())
                        .image(socialUserInfo.getImageUrl())
                        .password(passwordEncoder.encode(socialUserInfo.getNickname()))
                        .role(UNREGISTERED)
                        .build());
        return userRepository.save(user);
    }

    public abstract void deleteUser(String id, String code);

}