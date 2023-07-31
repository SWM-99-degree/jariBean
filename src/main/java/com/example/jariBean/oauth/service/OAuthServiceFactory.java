package com.example.jariBean.oauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuthServiceFactory {

    private final OAuthKakaoService oauthKakaoService;
    private final OAuthGoogleService oAuthGoogleService;

    public OAuthService get(String registration) {
        switch (registration) {
            case "kakao":
                return oauthKakaoService;
            case "google":
                return oAuthGoogleService;
            case "apple":
                return null;
            default:
                return null;
        }
    }
}