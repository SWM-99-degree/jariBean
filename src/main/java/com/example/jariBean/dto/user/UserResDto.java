package com.example.jariBean.dto.user;

import com.example.jariBean.entity.Role;
import com.example.jariBean.entity.User;
import com.example.jariBean.util.CustomDateUtil;
import lombok.*;

public class UserResDto {
    @Getter
    @Setter
    public static class UserLoginResDto {
        private String id;
        private String nickname;
        private String createdAt;

        public UserLoginResDto(User user) {
            this.id = user.getId();
            this.nickname = user.getNickname();
            this.createdAt = CustomDateUtil.toStringFormat(user.getCreatedAt());
        }
    }

    @Getter
    @Setter
    @ToString
    public static class UserJoinRespDto {
        private String id;
        private String nickname;

        public UserJoinRespDto(User user) {
            this.id = user.getId();
            this.nickname = user.getNickname();
        }
    }

    @Data
    public static class UserInfoRespDto {
        private String id;
        private String nickname;
        private String imageUrl;
        private String description;
        private Role role;

        @Builder
        public UserInfoRespDto(String id, String nickname, String imageUrl, String description, Role role) {
            this.id = id;
            this.nickname = nickname;
            this.imageUrl = imageUrl;
            this.description = description;
            this.role = role;
        }
    }
}
