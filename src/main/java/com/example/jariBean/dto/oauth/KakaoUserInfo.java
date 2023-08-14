package com.example.jariBean.dto.oauth;

import lombok.*;

import java.time.LocalDateTime;

@Data
public class KakaoUserInfo {
    private String id;
    private LocalDateTime connected_at;
    private Properties properties;
    private KakaoAccount kakao_account;

    @Data
    public static class Profile {
        private String nickname;
        private String thumbnail_image_url;
        private String profile_image_url;
        private boolean is_default_image;
    }

    @Data
    public static class Properties {
        private String nickname;
        private String profile_image;
        private String thumbnail_image;
    }

    @Data
    public static class KakaoAccount {
        private boolean profile_nickname_needs_agreement;
        private boolean profile_image_needs_agreement;
        private Profile profile;
    }
}