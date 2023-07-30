package com.example.jariBean.oauth.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class KakaoOAuthInfo {
    private String token_type;
    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String refresh_token_expires_in;
    private String scope;
}