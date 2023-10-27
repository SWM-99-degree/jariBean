package com.example.jariBean.dto.reserved;

import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.Reserved;
import com.example.jariBean.entity.Table;
import com.example.jariBean.entity.User;
import com.example.jariBean.handler.constraintValidator.CustomConstraint;
import com.example.jariBean.handler.ex.CustomApiException;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.example.jariBean.entity.Reserved.ReservedStatus.VALID;

@Slf4j
public class ReserveReqDto {

    @Getter
    @Setter
    public static class ReserveNearestReqDto {

        @NotEmpty(message = "userId는 필수입니다")
        private String userId;

        @NotEmpty(message = "유저의 시간은 필수입니다.")
        private LocalDateTime userNow;

    }

    @Getter
    @Setter
    @CustomConstraint
    public static class ReserveTableListReqDto extends TimeDto {

        @NotEmpty(message = "userId는 필수입니다")
        private String cafeId;

    }

    @Getter
    @Setter
    @CustomConstraint
    public static class ReserveSaveReqDto extends TimeDto {

        @NotEmpty(message = "cafeId는 필수입니다")
        private String cafeId;

        @NotEmpty(message = "tableId는 필수입니다")
        private String tableId;

        public void checkTimeStatus() {
            if (!(super.startTime.getMinute() == 0 || super.startTime.getMinute() == 30) ||!(super.endTime.getMinute() == 0 || super.endTime.getMinute() == 30)) {
                throw new CustomApiException("reservedStartTime 혹은 reservedEndTime의 형식이 올바르지 않습니다.");
            }
        }

        public Reserved toEntity(User user, Table table, Cafe cafe) {
            return Reserved.builder()
                    .user(user)
                    .cafe(cafe)
                    .table(table)
                    .startTime(super.startTime)
                    .endTime(super.endTime)
                    .status(VALID)
                    .build();
        }
    }

    @Data
    @CustomConstraint
    public static class TimeDto {
        @NotNull(message = "reservedStartTime은 필수입니다")
        private LocalDateTime startTime;

        @NotNull(message = "reservedStartTime은 필수입니다")
        private LocalDateTime endTime;
    }
}