package com.example.jariBean.dto.user;

import com.example.jariBean.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.example.jariBean.entity.User.UserRole.CUSTOMER;

@Slf4j
public class UserReqDto {

    @Getter
    @Setter
    public static class UserJoinReqDto {

        @NotEmpty(message = "username은 필수입니다")
        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자 2~20자 이내로 작성해주세요.")
        private String username;

        @NotEmpty(message = "userPhoneNumber는 필수입니다.")
        @Size(min = 11, max = 11)
        private String userPhoneNumber;

        @NotEmpty(message = "userPassword는 필수입니다")
        @Size(min = 4, max = 20) // 패스워드 인코딩 때문에
        private String userPassword;

        @NotEmpty(message = "userNickname 필수입니다")
        @Pattern(regexp = "^[a-zA-Z가-힣]{1,20}$", message = "한글/영문 1~20자 이내로 작성해주세요.")
        private String userNickname;

        @NotEmpty(message = "userNickname 필수입니다")
        @Pattern(regexp = "^[a-zA-Z가-힣]{1,20}$", message = "한글/영문 1~20자 이내로 작성해주세요.")
        private String socialId;

        public User toEntity(PasswordEncoder passwordEncoder) {

            return User.builder()
                    .nickname(username)
                    .nickname(userNickname)
                    .socialId(socialId)
                    .role(CUSTOMER)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class UserLoginReqDto {
        @NotEmpty(message = "필수: userId")
        private String userId;

        @NotEmpty(message = "필수: userName")
        private String userName;

        @NotEmpty(message = "필수: firebaseToken")
        private String firebaseToken;

        @Builder
        public UserLoginReqDto(String userId, String userName, String firebaseToken) {
            this.userId = userId;
            this.userName = userName;
            this.firebaseToken = firebaseToken;
        }
    }

}
