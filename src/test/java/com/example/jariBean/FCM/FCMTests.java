package com.example.jariBean.FCM;


import com.example.jariBean.service.FCMNotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FCMTests {

    @Autowired
    FCMNotificationService fcmNotificationService;


    @Test
    public void FCMTest() throws Exception{

        fcmNotificationService.sendNotificationByTokentest("eiMZvMU4TvCk4BNeUEHBoz:APA91bH7pZYQBC_aTV0RR4Y2uvrvN1-5_kTrhZWkpzp3G5r03r86JuqRh3fPThKlgVNTQb_kp7_wsnVfQVh9x0XbiZscqSR6XT62CZ28m1SkBsnKmN12rlSHlDnqnS-v-7Z1gFGXxAv3");

    }
}
