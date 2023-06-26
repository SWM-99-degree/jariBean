package com.example.jariBean.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.jariBean.config.auth.LoginCafe;
import com.example.jariBean.config.auth.LoginUser;
import com.example.jariBean.config.jwt.jwtdto.JwtDto;
import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.User;
import com.example.jariBean.entity.User.UserRole;
import com.example.jariBean.handler.ex.CustomForbiddenException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class JwtProcess {

    // create access JWT
    public static String create(LoginUser loginUser) {

        User user = loginUser.getUser();

        String jwt = JWT.create()
                .withSubject("jariBean")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.ACCESS_EXPIRATION_TIME))
                .withClaim("userId", user.getId())
                .withClaim("userRole", user.getUserRole().toString())
                .sign(Algorithm.HMAC512(JwtVO.SECRET));

        return JwtVO.TOKEN_PREFIX + jwt;
    }

    // 오버로드
    public static String create(LoginCafe loginCafe) {

        Cafe user = loginCafe.getUser();

        String jwt = JWT.create()
                .withSubject("jariBean")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.ACCESS_EXPIRATION_TIME))
                .withClaim("userId", user.getId())
                .withClaim("userRole", user.getUserRole().toString())
                .sign(Algorithm.HMAC512(JwtVO.SECRET));

        return JwtVO.TOKEN_PREFIX + jwt;
    }

    // create refresh JWT
    public static String createRefreshToken(LoginUser loginUser) {

        User user = loginUser.getUser();

        String jwt = JWT.create()
                .withSubject("jariBean")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.REFRESH_EXPIRATION_TIME))
                .withClaim("userId", user.getId())
                .withClaim("userRole", user.getUserRole().toString())
                .sign(Algorithm.HMAC512(JwtVO.SECRET));

        return JwtVO.TOKEN_PREFIX + jwt;
    }

    public static String createRefreshToken(LoginCafe loginCafe) {

        Cafe user = loginCafe.getUser();

        String jwt = JWT.create()
                .withSubject("jariBean")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.REFRESH_EXPIRATION_TIME))
                .withClaim("userId", user.getId())
                .withClaim("userRole", user.getUserRole().toString())
                .sign(Algorithm.HMAC512(JwtVO.SECRET));

        return JwtVO.TOKEN_PREFIX + jwt;
    }


    // verify JWT (return 되는 LoginUser 객체를 강제로 시큐리티 세션에 직접 주입할 예정)
    public static JwtDto verify(String jwt) {

        List<String> verifiedDto = new ArrayList<>();

        DecodedJWT decodedJwt = JWT.require(Algorithm.HMAC512(JwtVO.SECRET)).build().verify(jwt);

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
