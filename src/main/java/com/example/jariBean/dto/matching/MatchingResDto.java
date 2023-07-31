package com.example.jariBean.dto.matching;

import com.example.jariBean.dto.cafe.CafeResDto;
import lombok.Getter;
import lombok.Setter;

public class MatchingResDto {

    @Getter
    @Setter
    public static class MatchingSummaryResDto {
        private String matchingId;
        private Integer matchingSeating;
        private CafeResDto.CafeSummaryDto cafeSummaryDto;
    }
}
