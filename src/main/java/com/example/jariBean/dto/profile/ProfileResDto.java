package com.example.jariBean.dto.profile;

import com.example.jariBean.entity.User;
import lombok.Getter;

public class ProfileResDto {

    @Getter
    public static class ProfileSummaryResDto{
        private String nickName;
        private String imageUrl;
        private String description;

        public ProfileSummaryResDto(User user) {
            this.nickName = user.getNickname();
            this.imageUrl = user.getImage();
            this.description = user.getDescription();
        }
    }
}
