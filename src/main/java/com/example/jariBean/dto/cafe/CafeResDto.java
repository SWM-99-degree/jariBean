package com.example.jariBean.dto.cafe;

import com.example.jariBean.entity.Cafe;
import com.example.jariBean.util.CustomDateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class CafeResDto {


    @Getter
    @Setter
    public static class CafeLoginResDto {
        private String id;
        private String cafePhoneNumber;
        private String createdAt;

        public CafeLoginResDto(Cafe cafe) {
            this.id = cafe.getId();
            this.cafePhoneNumber = cafe.getCafePhoneNumber();
            this.createdAt = CustomDateUtil.toStringFormat(cafe.getCreatedAt());
        }
    }



    @Getter
    @Setter
    @ToString
    public static class CafeJoinRespDto {
        private String id;
        private String cafename;

        public CafeJoinRespDto(Cafe cafe) {
            this.id = cafe.getId();
            this.cafename = cafe.getCafeName();

        }
    }
}
