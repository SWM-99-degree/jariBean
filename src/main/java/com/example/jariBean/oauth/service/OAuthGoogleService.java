package com.example.jariBean.oauth.service;

import com.example.jariBean.config.jwt.JwtProcess;
import com.example.jariBean.oauth.dto.GoogleOAuthInfo;
import com.example.jariBean.oauth.dto.GoogleUserInfo;
import com.example.jariBean.oauth.service.OAuthKakaoService.SocialUserInfo;
import com.example.jariBean.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
public class OAuthGoogleService extends OAuthService{

    @Value("${GOOGLE_CLIENT_ID}")
    private String GOOGLE_CLIENT_ID;

    @Value("${GOOGLE_CLIENT_SECRET}")
    private String GOOGLE_CLIENT_SECRET;

    @Value(("${GOOGLE_REDIRECT_URI}"))
    private String GOOGLE_REDIRECT_URI;

    private final String REGISTRATION = "google";

    public OAuthGoogleService(UserRepository userRepository, JwtProcess jwtProcess) {
        super(userRepository, jwtProcess);
    }

    @Override
    public String getAccessToken(String code) {

        MultiValueMap<String, String> bodyValue = new LinkedMultiValueMap<>();
        bodyValue.add("grant_type", "authorization_code");
        bodyValue.add("client_id", GOOGLE_CLIENT_ID);
        bodyValue.add("client_secret", GOOGLE_CLIENT_SECRET);
        bodyValue.add("redirect_uri", GOOGLE_REDIRECT_URI);
        bodyValue.add("code", code);

        WebClient client = WebClient.create();
        return client.post()
                .uri("https://oauth2.googleapis.com/token")
                .contentType(APPLICATION_FORM_URLENCODED)
                .bodyValue(bodyValue)
                .retrieve()
                .bodyToMono(GoogleOAuthInfo.class)
                .block()
                .getAccess_token();
    }

    @Override
    public SocialUserInfo getUserInfo(String accessToken) {
        WebClient client1 = WebClient.create();
        GoogleUserInfo userInfo = client1.get()
                .uri("https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + accessToken)
                .retrieve()
                .bodyToMono(GoogleUserInfo.class)
                .block();

        return SocialUserInfo.create(
                REGISTRATION,
                userInfo.getId(),
                userInfo.getName(),
                userInfo.getPicture()
        );
    }
}