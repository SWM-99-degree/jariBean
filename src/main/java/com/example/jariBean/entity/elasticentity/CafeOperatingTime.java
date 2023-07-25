package com.example.jariBean.entity.elasticentity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "cafe.operatingTime")
public class CafeOperatingTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String cafeId;

    private LocalDateTime openTime;

    private LocalDateTime closeTime;

    private List<DAY> restDays = new ArrayList<>();


    enum DAY {
        MON, TUE, WED, THU, FRI, SAT, SUN
    }
}
