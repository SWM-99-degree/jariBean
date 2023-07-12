package com.example.jariBean.service;

import com.example.jariBean.dto.firebasedto.FCMReqDto;
import com.example.jariBean.entity.User;
import com.example.jariBean.repository.TokenRepository;
import com.example.jariBean.repository.user.UserRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FCMNotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public void sendNotificationByToken(FCMReqDto fcmReqDto) throws FirebaseMessagingException {

        Optional<User> users = userRepository.findById(fcmReqDto.getUserId());

        if (users.isPresent()){
            for (User user : users.stream().toList()) {
                String token = tokenRepository.findById(fcmReqDto.getUserId()).get().getFirebaseToken();
                if (token != null) {
                    Notification notification = Notification.builder()
                            .setTitle("jariBean")
                            .setBody("123")
                            .build();

                    Message sendMessage = Message.builder()
                            .setToken(token)
                            .setNotification(notification)
                            .build();

                    firebaseMessaging.send(sendMessage);
                }

            }

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
                    .setNotification(notification)
                    .build();

            firebaseMessaging.send(sendMessage);
        }

    }



}