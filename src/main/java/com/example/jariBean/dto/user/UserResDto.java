package com.example.jariBean.dto.user;

import com.example.jariBean.entity.User;
import com.example.jariBean.util.CustomDateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class UserResDto {
    @Getter
    @Setter
    public static class UserLoginResDto {
        private String id;
        private String userPhoneNumber;
        private String createdAt;

        public UserLoginResDto(User user) {
            this.id = user.getId();
            this.userPhoneNumber = user.getUserPhoneNumber();
            this.createdAt = CustomDateUtil.toStringFormat(user.getCreatedAt());
        }
    }

    @Getter
    @Setter
    @ToString
    public static class UserJoinRespDto {
        private String id;
        private String username;
        private String userNickname;

        public UserJoinRespDto(User user) {
            this.id = user.getId();
            this.username = user.getUserName();
            this.userNickname = user.getUserNickname();
        }
    }
}
