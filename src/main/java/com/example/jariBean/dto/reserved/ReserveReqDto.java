package com.example.jariBean.dto.reserved;

import com.example.jariBean.entity.Cafe;
import com.example.jariBean.entity.Reserved;
import com.example.jariBean.entity.Table;
import com.example.jariBean.entity.User;
import com.example.jariBean.handler.ex.CustomApiException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    public static class ReserveTableListReqDto {

        @NotEmpty(message = "userId는 필수입니다")
        private String cafeId;

        @NotEmpty(message = "유저의 시간은 필수입니다.")
        private LocalDateTime startTime;

        @NotEmpty(message = "유저의 시간은 필수입니다.")
        private LocalDateTime endTime;



    }

    @Getter
    @Setter
    public static class ReserveSaveReqDto {

        @NotEmpty(message = "cafeId는 필수입니다")
        private String cafeId;

        @NotEmpty(message = "tableId는 필수입니다")
        private String tableId;

        @NotNull(message = "reservedStartTime은 필수입니다")
        private LocalDateTime startTime;

        @NotNull(message = "reservedStartTime은 필수입니다")
        private LocalDateTime endTime;

        public void checkTimeStatus() {
            if (!(startTime.getMinute() == 0 || startTime.getMinute() == 30) ||!(endTime.getMinute() == 0 || endTime.getMinute() == 30)) {
                throw new CustomApiException("reservedStartTime 혹은 reservedEndTime의 형식이 올바르지 않습니다.");
            }
        }

        public Reserved toEntity(User user, Table table, Cafe cafe) {
            return Reserved.builder()
                    .user(user)
                    .cafe(cafe)
                    .table(table)
                    .startTime(startTime)
                    .endTime(endTime)
                    .build();
        }
    }
}