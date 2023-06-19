package com.example.jariBean.config.jwt;

public interface JwtVO {
    // TODO SECRET 은 노출되면 안된다.
    String SECRET = "secretKey"; // HS256 대칭키
    String TOKEN_PREFIX = "BEARER ";

    String ACCESS_HEADER = "ACCESS_AUTHORIZATION";
    String REFRESH_HEADER = "REFRESH_AUTHORIZATION";

    int ACCESS_EXPIRATION_TIME = 1000 * 60 * 60 * 2; // 2시간
    int REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 14; // 2주
}
