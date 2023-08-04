package com.example.jariBean.dto.matching;

import com.example.jariBean.dto.cafe.CafeResDto;
import com.example.jariBean.entity.Matching;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class MatchingResDto {

    @Getter
    @Setter
    public static class MatchingSummaryResDto {
        private String id;
        private Integer seating;
        private LocalDateTime startTime;
        private CafeResDto.CafeSummaryDto cafeSummaryDto;

        public MatchingSummaryResDto(Matching matching){
            this.id = matching.getId();
            this.seating = matching.getNumber();
            this.startTime = matching.getMatchingTime();
            this.cafeSummaryDto = new CafeResDto.CafeSummaryDto(matching.getCafe());
        }
    }
}
