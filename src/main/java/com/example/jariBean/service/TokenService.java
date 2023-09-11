package com.example.jariBean.service;

import com.example.jariBean.config.jwt.JwtProcess;
import com.example.jariBean.dto.oauth.LoginResDto.LoginSuccessResDto;
import com.example.jariBean.entity.Role;
import com.example.jariBean.entity.Token;
import com.example.jariBean.handler.ex.CustomApiException;
import com.example.jariBean.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.jariBean.config.jwt.JwtProcess.TokenType.ACCESS;
import static com.example.jariBean.config.jwt.JwtProcess.TokenType.REFRESH;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JwtProcess jwtProcess;

    public LoginSuccessResDto renewJWT(String userId, Role role, String nickname, String refreshJWT) {

        // find token set
        Token fToken = tokenRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("userId에 해당하는 Token set이 존재하지 않습니다."));

        // create JWT
        String accessToken = jwtProcess.createJWT(userId, role.toString(), nickname, ACCESS);
        String refreshToken = jwtProcess.createJWT(userId, role.toString(), nickname, REFRESH);

        // renew JWT
        fToken.renewJWT(accessToken, refreshToken);
        tokenRepository.save(fToken);

        return LoginSuccessResDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
