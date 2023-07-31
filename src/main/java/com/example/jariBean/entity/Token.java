package com.example.jariBean.entity;

import com.example.jariBean.config.jwt.JwtVO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@Getter
@Setter
@RedisHash(value = "Token", timeToLive = JwtVO.REFRESH_EXPIRATION_TIME)
public class Token {

    @Id
    private String userId;
    private String accessToken;
    private String refreshToken;
    private String firebaseToken;


    @Builder
    public Token(String userId, String accessToken, String refreshToken, String firebaseToken) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.firebaseToken = firebaseToken;
    }

}