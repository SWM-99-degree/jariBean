package com.example.jariBean.dto.reserved;

import com.example.jariBean.entity.Reserved;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotEmpty;
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

        @NotEmpty(message = "예약의 시작 시간은 필수입니다.")
        private LocalDateTime reservedStartTime;

        @NotEmpty(message = "예약의 끝 시간은 필수입니다.")
        private LocalDateTime reservedEndTime;

        public Reserved toEntity() {
            return Reserved.builder()
                    .cafeId(cafeId)
                    //.tableId(tableId)
                    .reservedStartTime(reservedStartTime)
                    .reservedEndTime(reservedEndTime)
                    .build();
        }
    }
}