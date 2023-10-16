package com.example.jariBean.service.oauth;


import com.example.jariBean.config.jwt.JwtProcess;
import com.example.jariBean.dto.oauth.KakaoOAuthInfo;
import com.example.jariBean.dto.oauth.KakaoUserInfo;
import com.example.jariBean.handler.ex.CustomApiException;
import com.example.jariBean.repository.TokenRepository;
import com.example.jariBean.repository.user.UserRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.atomic.AtomicReference;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
public class OAuthKakaoService extends OAuthService {

    @Value("${KAKAO_CLIENT_ID}")
    private String KAKAO_CLIENT_ID;

    @Value("${KAKAO_REDIRECT_URI}")
    private String KAKAO_REDIRECT_URI;

    private final String REGISTRATION = "kakao";

    public OAuthKakaoService(UserRepository userRepository, JwtProcess jwtProcess, TokenRepository tokenRepository) {
        super(userRepository, tokenRepository, jwtProcess);
    }

    @Override
    public String getAccessToken(String code) {

        AtomicReference<KakaoOAuthInfo> responseReference = new AtomicReference<>();

        MultiValueMap<String, String> bodyValue = new LinkedMultiValueMap<>();
        bodyValue.add("grant_type", "authorization_code");
        bodyValue.add("client_id", KAKAO_CLIENT_ID);
        bodyValue.add("redirect_uri", KAKAO_REDIRECT_URI);
        bodyValue.add("code", code);

        WebClient client = WebClient.create();
        KakaoOAuthInfo kakaoOAuthInfo = client.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(APPLICATION_FORM_URLENCODED)
                .bodyValue(bodyValue)
                .retrieve()
                .bodyToMono(KakaoOAuthInfo.class)
                .doOnError(throwable -> {
                    throw new CustomApiException("KAKAO Access Token 발급 과정에서 오류가 발생하였습니다.");
                })
                .block();

        return kakaoOAuthInfo.getAccess_token();
    }

    @Override
    public SocialUserInfo getUserInfo(String accessToken) {
        WebClient client1 = WebClient.create();
        KakaoUserInfo userInfo = client1.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoUserInfo.class)
                .doOnError(throwable -> {
                    throw new CustomApiException("KAKAO User information 접근 과정에서 오류가 발생하였습니다.");
                })
                .block();

        return SocialUserInfo.create(
                REGISTRATION,
                userInfo.getId(),
                userInfo.getProperties().getNickname() != null ? userInfo.getProperties().getNickname() : "Guest",
                userInfo.getProperties().getProfile_image()
        );
    }

    @Override
    public void deleteUser(String id, String code) {
        userRepository.deleteById(id);
    }

    @Getter
    public static class SocialUserInfo {
        private String socialId;
        private String nickname;
        private String imageUrl;

        public static SocialUserInfo create(String registration, String id, String nickname, String imageUrl) {
            SocialUserInfo socialUserInfo = new SocialUserInfo();
            socialUserInfo.socialId = registration + "_" + id;
            socialUserInfo.nickname = nickname;
            socialUserInfo.imageUrl = imageUrl;
            return socialUserInfo;
        }
    }
}