package com.example.jariBean.dto.cafe;

import com.example.jariBean.dto.reserved.ReservedResDto.TableReserveResDto;
import com.example.jariBean.entity.Cafe;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
            this.imageUrl = cafe.getImage();
        }
    }

    @Getter
    @Setter
    public static class CafeDetailDto {
        private CafeSummaryDto cafeSummaryDto;
        private LocalDateTime openingHour;
        private LocalDateTime closingHour;
        private String phoneNumber;
        private String description;
        private String instagram;
        private String image;

        public CafeDetailDto(Cafe cafe) {
            this.cafeSummaryDto = new CafeSummaryDto(cafe);
            this.openingHour = cafe.getStartTime();
            this.closingHour = cafe.getEndTime();
            this.phoneNumber = cafe.getPhoneNumber();
            this.description = cafe.getDescription();
            this.instagram = cafe.getInstagram();
            this.image = cafe.getImage();
        }

    }

    @Setter
    @Getter
    public static class CafeDetailReserveDto {
        private CafeDetailDto cafeDetailDto;
        private List<TableReserveResDto> tableReserveResDtoList;


        public void addTable(TableReserveResDto tableReserveResDto){
            this.tableReserveResDtoList.add(tableReserveResDto);
        }
    }
}
