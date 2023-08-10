package com.example.jariBean.config.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class FCMConfig {

    @Value("${FIREBASE_CLASS_PATH}")
    private String FIREBASE_CLASS_PATH;

    @Value("${TYPE}")
    private String type;

    @Value("${PROJECT_ID}")
    private String project_id;

    @Value("${PRIVATE_KEY_ID}")
    private String private_key_id;

    @Value("${PRIVATE_KEY}")
    private String private_key;

    @Value("${CLIENT_EMAIL}")
    private String client_email;

    @Value("${CLIENT_ID}")
    private String client_id;

    @Value("${AUTH_URI}")
    private String auth_uri;

    @Value("${TOKEN_URI}")
    private String token_uri;

    @Value("${AUTH_PROVIDER_X509_CERT_URL}")
    private String auth_provider_x509_cert_url;

    @Value("${CLIENT_X509_CERT_URL}")
    private String client_x509_cert_url;

    @Value("${UNIVERSE_DOMAIN}")
    private String universe_domain;


    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {
        String jsonKey = "{\n" +
                "  \"type\": \""+ type +"\",\n" +
                "  \"project_id\": \""+ project_id +"\",\n" +
                "  \"private_key_id\": \""+ private_key_id +"\",\n" +
                "  \"private_key\": \""+ private_key +"\",\n" +
                "  \"client_email\": \"" + client_email + "\",\n" +
                "  \"client_id\": \""+client_id+ "\",\n" +
                "  \"auth_uri\": \""+ auth_uri +"\",\n" +
                "  \"token_uri\": \""+ token_uri +"\",\n" +
                "  \"auth_provider_x509_cert_url\": \"" + auth_provider_x509_cert_url +"\",\n" +
                "  \"client_x509_cert_url\": \""+ client_x509_cert_url +"\",\n" +
                "  \"universe_domain\": \""+ universe_domain +"\"\n" +
                "}";


        InputStream refreshToken = new ByteArrayInputStream(jsonKey.getBytes(StandardCharsets.UTF_8));

        FirebaseApp firebaseApp = null;
        List<FirebaseApp> firebaseAppList = FirebaseApp.getApps();

        if (firebaseAppList != null && !firebaseAppList.isEmpty()){
            for (FirebaseApp app : firebaseAppList){
                if (app.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
                {firebaseApp = app;}
            }
        } else {
            FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(refreshToken)).build();
            firebaseApp = FirebaseApp.initializeApp(options);
        }
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}