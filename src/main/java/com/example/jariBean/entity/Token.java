package com.example.jariBean.entity;

import com.example.jariBean.config.jwt.JwtVO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@Getter
@Setter
@RedisHash(timeToLive = JwtVO.REFRESH_EXPIRATION_TIME)
public class Token {

    @Id
    private String userPhoneNumber;
    private String refreshToken;
    private String firebaseToken;


    @Builder
    public Token(String userPhoneNumber, String refreshToken, String firebaseToken) {
        this.userPhoneNumber = userPhoneNumber;
        this.refreshToken = refreshToken;
        this.firebaseToken = firebaseToken;
    }

}