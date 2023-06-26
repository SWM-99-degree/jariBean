package com.example.jariBean.dto.cafe;

import com.example.jariBean.entity.Cafe;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.example.jariBean.entity.User.UserRole.MANAGER;

@Slf4j
public class CafeReqDto {

    @Getter
    @Setter
    public static class CafeLoginReqDto {
        @NotEmpty(message = "필수: cafePhoneNumber")
        @Size(min = 11, max = 11)
        private String cafePhoneNumber;

        @NotEmpty(message = "필수: password")
        @Size(min = 4, max = 20) // 패스워드 인코딩 때문에
        private String password;

    }

    @Getter
    @Setter
    public static class Location {
        @NotNull(message = "위도는 필수입니다")
        private double latitude; // 위도

        @NotNull(message = "경도는 필수입니다")
        private double longitude; // 경도

        // Getter, Setter, 생성자 등 필요한 메서드 추가
    }

    @Getter
    @Setter
    public static class CafeJoinReqDto {

        @NotEmpty(message = "카페 이름은 필수입니다")
        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자 2~20자 이내로 작성해주세요.")
        private String cafename;

        @NotEmpty(message = "cafePhoneNumber는 필수입니다.")
        @Size(min = 11, max = 11)
        private String cafePhoneNumber;

        @NotEmpty(message = "cafePassword는 필수입니다")
        @Size(min = 4, max = 20) // 패스워드 인코딩 때문에
        private String cafePassword;

        @NotEmpty(message = "cafeAddress는 필수입니다")
        @Pattern(regexp = "^[a-zA-Z가-힣\s]{1,20}$", message = "한글/영문 1~20자 이내로 작성해주세요.")
        private String cafeAddress;


        private Location location;

        public Cafe toEntity(PasswordEncoder passwordEncoder) {

            return Cafe.builder()
                    .cafeName(cafename)
                    .cafePhoneNumber(cafePhoneNumber)
                    .cafePassword(passwordEncoder.encode(cafePassword))
                    .cafeAddress(cafeAddress)
                    .userRole(MANAGER)
                    .latitude(location.latitude).longitude(location.longitude)
                    .build();
        }
    }

}
