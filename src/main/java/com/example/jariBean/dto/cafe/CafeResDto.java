package com.example.jariBean.dto.cafe;

import com.example.jariBean.dto.reserved.ReservedResDto.TableReserveResDto;
import com.example.jariBean.entity.Cafe;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class CafeResDto {

    @Getter
    @Setter
    public static class CafeSummaryDto {
        private String id;
        private String name;
        private String address;
        private String imageUrl;

        public CafeSummaryDto(Cafe cafe) {
            this.id = cafe.getId();
            this.name = cafe.getName();
            this.address = cafe.getAddress();
            this.imageUrl = cafe.getImageUrl();
        }
    }

    @Getter
    @Setter
    public static class CafeDetailDto {
        private CafeSummaryDto cafeSummaryDto;
        private LocalDateTime openingHours;
        private String phoneNumber;
        private String description;
        private String instagram;
        private String image;

    }

    @Setter
    public static class CafeDetailReserveDto {
        private CafeDetailDto cafeDetailDto;
        private List<TableReserveResDto> tableReserveResDtoList;
    }
}
