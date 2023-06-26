package com.example.jariBean.config.jwt;

import com.example.jariBean.config.auth.LoginUser;
import com.example.jariBean.dto.user.UserResDto;
import com.example.jariBean.entity.Token;
import com.example.jariBean.handler.ex.CustomApiException;
import com.example.jariBean.repository.TokenRepository;
import com.example.jariBean.util.CustomResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtRefreshAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private AuthenticationManager authenticationManager;
    private TokenRepository tokenRepository;


    public JwtRefreshAuthenticationFilter(AuthenticationManager authenticationManager, TokenRepository tokenRepository) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/users/refreshtoken"); // 고객 로그인 URL 지정
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            // RefreshToken을 받아서 Redis에서 검증하는 단계
            String refreshToken = request.getHeader("REFRESH_HEADER");
            String userId = JwtProcess.verify(refreshToken).getId();
            Token findToken = tokenRepository.findById(userId).orElseThrow(() -> new CustomApiException("cant find"));
            String authorization_Token = findToken.getRefreshToken();

            // 결국 통과하게 되면 successfulAuthentication 로 이동
            Authentication fakeAuthentication = new UsernamePasswordAuthenticationToken("fakeUser", "fakePassword");
            return fakeAuthentication;
        } catch (Exception e) {
            // JwtAuthenticationFilter.unsuccessfulAuthentication 메서드 호출
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    // 로그인 실패
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        CustomResponseUtil.fail(response, "토큰 갱신 불가능", HttpStatus.UNAUTHORIZED);
    }

    // JwtAuthenticationFilter.attemptAuthentication 메서드가 잘 실행되면 successfulAuthentication 메서드 이어서 실행
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        LoginUser loginUser = (LoginUser) authResult.getPrincipal();

        // 새로운 refresh, access token 생성
        String accessToken = JwtProcess.create(loginUser);
        String refreshToken = JwtProcess.createRefreshToken(loginUser);

        // redis에 갱신함
        Token redisToken = tokenRepository.findById(loginUser.getUsername()).get();
        redisToken.setRefreshToken(refreshToken);
        tokenRepository.save(redisToken);

        response.addHeader(JwtVO.ACCESS_HEADER, accessToken);
        response.addHeader(JwtVO.REFRESH_HEADER, refreshToken);

        UserResDto.UserLoginResDto loginResDto = new UserResDto.UserLoginResDto(loginUser.getUser());
        CustomResponseUtil.success(response, loginResDto);
    }
}
