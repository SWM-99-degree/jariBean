package com.example.jariBean.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuthServiceFactory {

    private final OAuthKakaoService oauthKakaoService;
    private final OAuthGoogleService oAuthGoogleService;
    private final OAuthAppleService oAuthAppleService;

    public OAuthService get(String registration) {
        switch (registration) {
            case "kakao":
                return oauthKakaoService;
            case "google":
                return oAuthGoogleService;
            case "apple":
                return oAuthAppleService;
            default:
                return oauthKakaoService;
        }
    }
}