package com.example.jariBean.controller;

import com.example.jariBean.service.UserService;
import com.example.jariBean.service.oauth.OAuthService;
import com.example.jariBean.service.oauth.OAuthServiceFactory;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OAuthController {

    private OAuthService oAuthService;
    private final UserService userService;
    private final OAuthServiceFactory authServiceFactory;

    @Operation(summary = "return social code", description = "api for return social code")
    @GetMapping("/login/oauth2/code/{registrationId}")
    public String returnCode(@RequestParam("code") String code) {
        log.info("social login code={}", code);
        return "redirect:jaribean://code?code=" + code;
    }

}