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
    public static class Location {
        @NotNull(message = "위도는 필수입니다")
        private double latitude; // 위도

        @NotNull(message = "경도는 필수입니다")
        private double longitude; // 경도

        // Getter, Setter, 생성자 등 필요한 메서드 추가
    }

}
