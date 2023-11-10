package com.example.jariBean.service;

import com.example.jariBean.dto.fcm.FCMReqDto.FCMTokenReqDto;
import com.example.jariBean.entity.Token;
import com.example.jariBean.handler.ex.CustomApiException;
import com.example.jariBean.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final TokenRepository tokenRepository;


    public void updateFirebaseToken(String userId, FCMTokenReqDto fcmTokenReqDto) {
        // find token by userId
        userId = "Token:" + userId;
        System.out.println(userId);
        Token token = tokenRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("userId에 해당하는 토큰을 찾을 수 없습니다."));

        // update firebase token
        token.updateFcmToken(fcmTokenReqDto.getFirebaseToken());
        tokenRepository.save(token);
    }

}
