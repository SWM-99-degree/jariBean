package com.example.jariBean.config.jwt;

import com.example.jariBean.config.auth.LoginCafe;
import com.example.jariBean.dto.cafe.CafeReqDto;
import com.example.jariBean.dto.cafe.CafeResDto;
import com.example.jariBean.entity.Token;
import com.example.jariBean.repository.TokenRepository;
import com.example.jariBean.util.CustomResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
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

public class JwtAuthenticationCafeFilter extends UsernamePasswordAuthenticationFilter {


    private AuthenticationManager authenticationManager;
    private TokenRepository tokenRepository;


    public JwtAuthenticationCafeFilter(AuthenticationManager authenticationManager, TokenRepository tokenRepository) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/cafes/login"); // 고객 로그인 URL 지정
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            ObjectMapper mapper = new ObjectMapper();
            CafeReqDto.CafeLoginReqDto loginReqDto = mapper.readValue(request.getInputStream(), CafeReqDto.CafeLoginReqDto.class);
            // 강제 로그인
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginReqDto.getCafePhoneNumber(), loginReqDto.getPassword());
            // UserDetailsService.LoadByUsername 호출
            // JWT를 쓴다고 하더라도, 컨트롤러에 진입을 하면 시큐리티 권한 체크, 인증 체크의 도움을 받을 수 있게 세션을 만든다.
            // 이 세션의 유효기간은 request ~ response 까지이다.
            return authenticationManager.authenticate(authenticationToken);
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

        LoginCafe loginUser = (LoginCafe) authResult.getPrincipal();

        // 새로운 refresh, access token 생성, 오버로드된 메서드로 들어감
        String accessToken = JwtProcess.create(loginUser);
        String refreshToken = JwtProcess.createRefreshToken(loginUser);
        String firebaseToken;

        // firebase 가져오기
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            firebaseToken = authorizationHeader.substring(7); // "Bearer " 이후의 토큰 값 추출
        } else {
            firebaseToken = null;
        }

        // redis에 설정하기
        Token redisToken = new Token(loginUser.getUsername(), refreshToken, firebaseToken);
        tokenRepository.save(redisToken);

        response.addHeader(JwtVO.ACCESS_HEADER, accessToken);
        response.addHeader(JwtVO.REFRESH_HEADER, refreshToken);

        CafeResDto.CafeLoginResDto loginResDto = new CafeResDto.CafeLoginResDto(loginUser.getUser());
        CustomResponseUtil.success(response, loginResDto);
    }
}
