package com.example.jariBean.dto.oauth;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AppleOAuthInfo {
    private String access_token;
    private String token_type;
    private Integer expires_in;
    private String refresh_token;
    private String id_token;
}
