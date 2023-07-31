package com.example.jariBean.dto.home;

import com.example.jariBean.dto.cafe.CafeResDto.CafeSummaryDto;
import com.example.jariBean.dto.reserved.ReservedResDto.ReserveSummaryResDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HomeResDto {
    private ReserveSummaryResDto reserveSummaryDto;
    private List<CafeSummaryDto> cafeSummaryDtoList;
}
