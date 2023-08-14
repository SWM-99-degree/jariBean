package com.example.jariBean.controller;

import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.oauth.LoginCode;
import com.example.jariBean.dto.oauth.LoginResDto.LoginSuccessResDto;
import com.example.jariBean.service.oauth.OAuthKakaoService.SocialUserInfo;
import com.example.jariBean.service.oauth.OAuthService;
import com.example.jariBean.service.oauth.OAuthServiceFactory;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;


@Controller
@RequiredArgsConstructor
public class OAuthController {

    private OAuthService oAuthService;
    private final OAuthServiceFactory authServiceFactory;

    @Operation(summary = "return social code", description = "api for return social code")
    @GetMapping("/login/oauth2/code/{registrationId}")
    public String returnCode(@RequestParam("code") String code) {
        return "redirect:jaribean://code?code=" + code;
    }

    @Operation(summary = "social login", description = "api for social login")
    @PostMapping("/login/{registrationId}")
    public ResponseEntity login(@PathVariable("registrationId") String registrationId,
                                        @RequestBody LoginCode loginCode) {

        // assign an oauthService corresponding to the registrationId
        oAuthService = authServiceFactory.get(registrationId);

        // get accessToken using code
        String accessToken = oAuthService.getAccessToken(loginCode.getCode());

        // get user information using accessToken
        SocialUserInfo socialUserInfo = oAuthService.getUserInfo(accessToken);

        // save or update oauth information
        LoginSuccessResDto loginSuccessResDto = oAuthService.saveOrUpdate(socialUserInfo, registrationId);

        return new ResponseEntity<>(new ResponseDto<>(1, "로그인 성공", loginSuccessResDto), OK);
    }

}