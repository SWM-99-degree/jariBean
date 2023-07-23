package com.example.jariBean.oauth;

import com.example.jariBean.config.jwt.JwtProcess;
import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.entity.User;
import com.example.jariBean.oauth.dto.GoogleOAuthInfo;
import com.example.jariBean.oauth.dto.GoogleUserInfo;
import com.example.jariBean.oauth.dto.KakaoOAuthInfo;
import com.example.jariBean.oauth.dto.KakaoUserInfo;
import com.example.jariBean.repository.user.UserRepository;
import com.example.jariBean.util.CustomResponseUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.MediaType.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/login/oauth2/code")
public class OAuthController {

    private final OAuthService oAuthService;
    private final UserRepository userRepository;

    @Value("${KAKAO_CLIENT_ID}")
    private String KAKAO_CLIENT_ID;

    @Value("${KAKAO_REDIRECT_URI}")
    private String KAKAO_REDIRECT_URI;

    @Value("${GOOGLE_CLIENT_ID}")
    private String GOOGLE_CLIENT_ID;

    @Value("${GOOGLE_CLIENT_SECRET}")
    private String GOOGLE_CLIENT_SECRET;

    @Value(("${GOOGLE_REDIRECT_URI}"))
    private String GOOGLE_REDIRECT_URI;

    // https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=ad927e3ff571a0fee70627513d0ba64b&redirect_uri=http://localhost:8080/login/oauth2/code/kakao
    @GetMapping("/{registrationId}")
    public ResponseEntity authorize(@PathVariable("registrationId") String registrationId,
                                    @RequestParam("code") String code,
                                    HttpServletResponse response) {

        if(registrationId.equals("kakao")) {
            MultiValueMap<String, String> bodyValue = new LinkedMultiValueMap<>();
            bodyValue.add("grant_type", "authorization_code");
            bodyValue.add("client_id", KAKAO_CLIENT_ID);
            bodyValue.add("redirect_uri", KAKAO_REDIRECT_URI);
            bodyValue.add("code", code);

            WebClient client = WebClient.create();
            KakaoOAuthInfo kakaoOAuthToken = client.post()
                    .uri("https://kauth.kakao.com/oauth/token")
                    .contentType(APPLICATION_FORM_URLENCODED)
                    .bodyValue(bodyValue)
                    .retrieve()
                    .bodyToMono(KakaoOAuthInfo.class)
                    .block();

            WebClient client1 = WebClient.create();
            KakaoUserInfo kakaoUserInfo = client1.get()
                    .uri("https://kapi.kakao.com/v2/user/me")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoOAuthToken.getAccess_token())
                    .retrieve()
                    .bodyToMono(KakaoUserInfo.class)
                    .block();

            String id = kakaoUserInfo.getId();
            String nickname = kakaoUserInfo.getProperties().getNickname();
            String imageUrl = kakaoUserInfo.getProperties().getProfile_image();

            String socialId = oAuthService.createSocialId(registrationId, id);
            User user = oAuthService.findByUserSocialId(socialId, nickname, imageUrl);

            String accessToken = JwtProcess.create(user);
            String refreshToken = JwtProcess.createRefreshToken(user);

            response.addHeader("accessToken", accessToken);
            response.addHeader("refreshToken", refreshToken);

            CustomResponseUtil.success(response, null);

        }

        // https://accounts.google.com/o/oauth2/v2/auth?client_id=611000095748-o28ik38acmp5j03pgf53rvk2ndc49gki.apps.googleusercontent.com&redirect_uri=http://localhost:8080/login/oauth2/code/google&response_type=code&scope=openid https://www.googleapis.com/auth/userinfo.profile

        if(registrationId.equals("google")) {

            MultiValueMap<String, String> bodyValue = new LinkedMultiValueMap<>();
            bodyValue.add("grant_type", "authorization_code");
            bodyValue.add("client_id", GOOGLE_CLIENT_ID);
            bodyValue.add("client_secret", GOOGLE_CLIENT_SECRET);
            bodyValue.add("redirect_uri", GOOGLE_REDIRECT_URI);
            bodyValue.add("code", code);

            WebClient client = WebClient.create();
            GoogleOAuthInfo googleOAuthInfo = client.post()
                    .uri("https://oauth2.googleapis.com/token")
                    .contentType(APPLICATION_FORM_URLENCODED)
                    .bodyValue(bodyValue)
                    .retrieve()
                    .bodyToMono(GoogleOAuthInfo.class)
                    .block();

            System.out.println("googleOAuthInfo.toString() = " + googleOAuthInfo.toString());

            WebClient client1 = WebClient.create();
            GoogleUserInfo googleUserInfo = client1.get()
                    .uri("https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + googleOAuthInfo.getAccess_token())
                    .retrieve()
                    .bodyToMono(GoogleUserInfo.class)
                    .block();

            String id = googleUserInfo.getId();
            String socialId = oAuthService.createSocialId(registrationId, id);
            String googleNickname = googleUserInfo.getName();
            String googleImageUrl = googleUserInfo.getPicture();

            User user = oAuthService.findByUserSocialId(socialId, googleNickname, googleImageUrl);

            String accessToken = JwtProcess.create(user);
            String refreshToken = JwtProcess.createRefreshToken(user);

            response.addHeader("accessToken", accessToken);
            response.addHeader("refreshToken", refreshToken);

            System.out.println("accessToken = " + accessToken);

            CustomResponseUtil.success(response, null);

        }
        return null;
    }

}
