package com.example.jariBean.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@NoArgsConstructor
public class CafeOperatingTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(unique = true, nullable = false)
    private String cafeId;

    // 매장 오픈 시간
    private LocalDateTime openTime;

    // 매장 마감 시간
    private LocalDateTime closeTime;

    // 쉬는날
    private List<DAY> restDays = new ArrayList<>();

    enum DAY {
        MON, TUE, WED, THU, FRI, SAT, SUN
    }

}
