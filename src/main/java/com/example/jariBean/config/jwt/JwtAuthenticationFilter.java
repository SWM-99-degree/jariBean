package com.example.jariBean.config.jwt;

import com.example.jariBean.config.auth.LoginCafe;
import com.example.jariBean.dto.manager.ManagerReqDto.ManagerLoginReqDto;
import com.example.jariBean.dto.oauth.LoginResDto.LoginSuccessResDto;
import com.example.jariBean.entity.Token;
import com.example.jariBean.repository.TokenRepository;
import com.example.jariBean.util.CustomResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final JwtProcess jwtProcess;
    private AuthenticationManager authenticationManager;
    private TokenRepository tokenRepository;
    private ThreadLocal<ManagerLoginReqDto> requestBodyHolder = new ThreadLocal<>();



    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, TokenRepository tokenRepository, JwtProcess jwtProcess) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/manager/login"); // 고객 로그인 URL 지정
        this.authenticationManager = authenticationManager;
        this.tokenRepository = tokenRepository;
        this.jwtProcess = jwtProcess;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        try {
            ManagerLoginReqDto loginReqDto = mapper.readValue(request.getInputStream(), ManagerLoginReqDto.class);

            // 강제 로그인
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginReqDto.getEmail(), loginReqDto.getPassword());
            // UserDetailsService.LoadByUsername 호출
            // JWT를 쓴다고 하더라도, 컨트롤러에 진입을 하면 시큐리티 권한 체크, 인증 체크의 도움을 받을 수 있게 세션을 만든다.
            // 이 세션의 유효기간은 request ~ response 까지이다.

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            requestBodyHolder.set(loginReqDto);

            return authentication;
        } catch (Exception e) {
            // JwtAuthenticationFilter.unsuccessfulAuthentication 메서드 호출
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    // 로그인 실패
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        CustomResponseUtil.fail(response, "로그인 실패", HttpStatus.UNAUTHORIZED);
    }

    // JwtAuthenticationFilter.attemptAuthentication 메서드가 잘 실행되면 successfulAuthentication 메서드 이어서 실행
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        ManagerLoginReqDto managerLoginReqDto = requestBodyHolder.get();

        LoginCafe loginCafe = (LoginCafe) authResult.getPrincipal();

        // refresh, access token 생성
        String accessToken = jwtProcess.createJWT(
                loginCafe.getCafeManager().getCafeId(),
                loginCafe.getCafeManager().getRole().toString(),
                JwtProcess.TokenType.ACCESS);

        String refreshToken = jwtProcess.createJWT(
                loginCafe.getCafeManager().getCafeId(),
                loginCafe.getCafeManager().getRole().toString(),
                JwtProcess.TokenType.REFRESH);

        // `Token` 생성
        Token token = Token.builder()
                .userId(loginCafe.getCafeManager().getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        // `Token` 저장
        tokenRepository.save(token);

        // refresh, access token 헤더에 추가
        response.addHeader(JwtVO.ACCESS_HEADER, accessToken);
        response.addHeader(JwtVO.REFRESH_HEADER, refreshToken);

        LoginSuccessResDto loginSuccessResDto = LoginSuccessResDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        CustomResponseUtil.success(response, loginSuccessResDto);
    }

}
