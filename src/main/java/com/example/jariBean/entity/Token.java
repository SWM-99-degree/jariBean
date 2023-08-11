package com.example.jariBean.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@Getter
@Setter
@RedisHash(value = "Token")
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