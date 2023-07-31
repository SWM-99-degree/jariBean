package com.example.jariBean.oauth;

import com.example.jariBean.entity.User;
import com.example.jariBean.oauth.service.OAuthKakaoService.SocialUserInfo;
import com.example.jariBean.oauth.service.OAuthService;
import com.example.jariBean.oauth.service.OAuthServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;


@Controller
@RequiredArgsConstructor
public class OAuthController {

    private OAuthService oAuthService;
    private final OAuthServiceFactory authServiceFactory;

    @GetMapping("/login/oauth2/code/{registrationId}")
    public String authorize(@PathVariable("registrationId") String registrationId,
                            @RequestParam("code") String code,
                            RedirectAttributes redirect) throws IOException {

        // registrationId에 해당하는 oauthService 할당
        oAuthService = authServiceFactory.get(registrationId);

        // code를 이용해 accessToken 요청
        String accessToken1 = oAuthService.getAccessToken(code);

        // accessToken을 이용해 유저 정보(id, nickname, imageUrl)를 요청
        SocialUserInfo userInfo = oAuthService.getUserInfo(accessToken1);

        // 유저 정보(id, nickname, imageUrl)로 로그인
        User user = oAuthService.saveOrUpdate(userInfo);

        redirect.addAttribute("userId", user.getSocialId());
        redirect.addAttribute("nickName", user.getNickname());

        return "redirect:jaribean://login";
    }

}