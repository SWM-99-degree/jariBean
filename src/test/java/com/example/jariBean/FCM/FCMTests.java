package com.example.jariBean.FCM;


import com.example.jariBean.service.FCMNotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

@SpringBootTest
public class FCMTests {

    @Autowired
    FCMNotificationService fcmNotificationService;

    @Value("${FIREBASE_CLASS_PATH}")
    private String FIREBASE_CLASS_PATH;
    @Test
    public void FCMTest() throws Exception{



        fcmNotificationService.sendNotificationByTokentest("eiMZvMU4TvCk4BNeUEHBoz:APA91bG6uf_mg9I70YslVe4E6nOvrP6pvFkZ8BVIF-8YDnfqYM0tLNQYtMG6pVFdaHCBWWwEbsRBZg5GJ4MHp6RBTgufDOrXovJYxz53xGPWTXpLAEbfTtTmTXV7dtKR8PDENqpOPF74");

    }
}
