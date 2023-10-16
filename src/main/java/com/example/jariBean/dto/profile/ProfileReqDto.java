package com.example.jariBean.dto.profile;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

public class ProfileReqDto {
    @Getter
    @Setter
    public static class ProfileUpdateReqDto {

        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자 2~20자 이내로 작성해주세요.")
        private String nickname;

        private String imageUrl;

        @Pattern(regexp = ".{2,50}$", message = "50자 이내로 작성해주세요.")
        private String description;
    }


}
