package com.example.jariBean.dto.cafe;

import com.example.jariBean.entity.TableClass.TableOption;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class CafeReqDto {

    @Getter
    @Setter
    public static class Location {

        private double latitude; // 위도


        private double longitude; // 경도

        // Getter, Setter, 생성자 등 필요한 메서드 추가
    }

    @Getter
    @Setter
    public static class CafeSearchReqDto {
        private String searchingWord;

        private Location location;

        @NotNull(message = "예약 시작 시간은 필수입니다.")
        private LocalDateTime reserveStartTime;

        @NotNull(message = "예약 끝 시간 필수입니다.")
        private LocalDateTime reserveEndTime;

        @NotNull(message = "인원수는 필수입니다.")
        private Integer peopleNumber;

        private List<TableOption> tableOptionList;
    }

}
