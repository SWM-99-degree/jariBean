package com.example.jariBean.dto.reserved;

import com.example.jariBean.entity.Reserved;
import com.example.jariBean.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.time.LocalDateTime;

import static com.example.jariBean.entity.User.UserRole.CUSTOMER;

@Slf4j
public class ReservedReqDto {

    @Getter
    @Setter
    public static class NearestReservedReqDto {

        @NotEmpty(message = "userId는 필수입니다")
        private String userId;

        @NotEmpty(message = "유저의 시간은 필수입니다.")
        private LocalDateTime userNow;

    }

    @Getter
    @Setter
    public static class ReservedTableListReqDto {

        @NotEmpty(message = "userId는 필수입니다")
        private String userId;

        @NotEmpty(message = "유저의 시간은 필수입니다.")
        private String userNow;

    }

    @Getter
    @Setter
    public static class SaveReservedReqDto {

        @NotEmpty(message = "cafeId는 필수입니다")
        private String cafeId;

        @NotEmpty(message = "userId는 필수입니다")
        private String userId;

        @NotEmpty(message = "tableId는 필수입니다")
        private String tableId;


        @NotEmpty(message = "예약의 시작 시간은 필수입니다.")
        private LocalDateTime reservedStartTime;

        @NotEmpty(message = "예약의 끝 시간은 필수입니다.")
        private LocalDateTime reservedEndTime;

        public Reserved toEntity() {

            return Reserved.builder()
                    .userId(userId)
                    .cafeId(cafeId)
                    .tableId(tableId)
                    .reservedStartTime(reservedStartTime)
                    .reservedEndTime(reservedEndTime)
                    .build();
        }

        }
}