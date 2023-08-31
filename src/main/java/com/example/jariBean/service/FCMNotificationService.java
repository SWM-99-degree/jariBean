package com.example.jariBean.service;

import com.example.jariBean.handler.ex.CustomApiException;
import com.example.jariBean.repository.TokenRepository;
import com.example.jariBean.repository.user.UserRepository;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FCMNotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public void sendNotification930(List<String> userIds) throws FirebaseMessagingException {
        List<String> tokenList = new ArrayList<>();
        if (!userIds.isEmpty()) {
            // resemble Token
            for (String userId : userIds) {
                tokenList.add(tokenRepository.findById(userId)
                        .orElseThrow(() -> new CustomApiException("userId에 해당하는 Token set이 존재하지 않습니다.")).getFirebaseToken());
            }

            // Send Message
            MulticastMessage sendMessage = MulticastMessage.builder()
                    .putData("title", "자리:Bean 예약 알림")
                    .putData("description", "오늘의 예약을 잊지 마세요!")
                    .addAllTokens(tokenList)
                    .build();

            BatchResponse response = firebaseMessaging.sendMulticast(sendMessage);
        }
    }


    // test 용
    public void sendNotificationByTokentest(String token) throws FirebaseMessagingException {
        if (token != null) {
            Notification notification = Notification.builder()
                    .setTitle("jariBean")
                    .build();
            Message sendMessage = Message.builder()
                    .setToken(token)
                    .putData("title", "hoho")
                    .putData("description", "description")
                    .build();

            firebaseMessaging.send(sendMessage);
        }

    }



}