package com.example.jariBean.dto.fcm;

import lombok.Data;

public class FCMReqDto {

    @Data
    public static class FCMTokenReqDto {
        private String firebaseToken;
    }
}
