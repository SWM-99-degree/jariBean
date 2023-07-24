package com.example.jariBean.dto.dbconnect;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Slf4j
@Getter
@Setter
public class CafeJoinOperatingTimeDto {

    private String cafeId;

    private String cafeName;

    private String cafePhoneNumber;

    private String cafeImg;
    // 매장 시작 시간
    private LocalDateTime openTime;
    // 매장 마감 시간
    private LocalDateTime closeTime;
}
