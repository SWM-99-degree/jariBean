package com.example.jariBean.service;

import com.example.jariBean.repository.reserved.ReservedRepository;
import com.example.jariBean.repository.user.UserRepository;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


@Component
@EnableAsync
@RequiredArgsConstructor
public class SchedulerService {

    private final FCMNotificationService fcmNotificationService;

    private final ReservedRepository reservedRepository;

    @Scheduled(cron = "0 30 9 * * *", zone = "Asia/Seoul")
    public void sendNearestReserveMessage() throws FirebaseMessagingException {
        List<String> userIds = reservedRepository.findReserveInNextDay(LocalDateTime.now());

        fcmNotificationService.sendNotification930(userIds);
    }
}
