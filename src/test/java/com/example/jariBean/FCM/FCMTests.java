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

        fcmNotificationService.sendNotificationByTokentest("토큰 내용");

    }
}
