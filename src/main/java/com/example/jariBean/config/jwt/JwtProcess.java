package com.example.jariBean.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.jariBean.config.jwt.jwtdto.JwtDto;
import com.example.jariBean.entity.User;
import com.example.jariBean.handler.ex.CustomForbiddenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProcess {

    @Value("${JWT_SECRET_KEY}")
    private String JWT_SECRET_KEY;

    // create access JWT
    public String createAccessToken(User user) {

        String jwt = JWT.create()
                .withSubject("jariBean")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.ACCESS_EXPIRATION_TIME))
                .withClaim("userId", user.getId())
                .withClaim("userRole", user.getRole().toString())
                .sign(Algorithm.HMAC512(JWT_SECRET_KEY));

        return jwt;
    }

    // create refresh JWT
    public String createRefreshToken(User user) {

        String jwt = JWT.create()
                .withSubject("jariBean")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.REFRESH_EXPIRATION_TIME))
                .withClaim("userId", user.getId())
                .withClaim("userRole", user.getRole().toString())
                .sign(Algorithm.HMAC512(JWT_SECRET_KEY));

        return jwt;
    }


    // verify JWT (return 되는 LoginUser 객체를 강제로 시큐리티 세션에 직접 주입할 예정)
    public JwtDto verify(String jwt) {

        DecodedJWT decodedJwt = JWT.require(Algorithm.HMAC512(JWT_SECRET_KEY)).build().verify(jwt);

        // 토큰 만료기간 검증
        Date expiresAt = decodedJwt.getExpiresAt();
        if(expiresAt.before(new Date())) {
            throw new CustomForbiddenException("토큰 사용기간이 만료되었습니다.");
        }

        String id = decodedJwt.getClaim("userId").asString();
        String userRole = decodedJwt.getClaim("userRole").asString();
        JwtDto jwtDto = new JwtDto(id, userRole);

        return jwtDto;

    }
}
