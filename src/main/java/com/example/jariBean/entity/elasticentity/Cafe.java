package com.example.jariBean.entity.elasticentity;


import lombok.*;
import org.springframework.data.elasticsearch.annotations.*;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "cafe.cafe")
@Mapping(mappingPath = "elastic/cafe-mapping.json")
@Setting(settingPath = "elastic/cafe-setting.json")
public class Cafe {

    @Id
    private String Id;

    @Field(type = FieldType.Date, format = {DateFormat.date_hour_minute_second_millis, DateFormat.epoch_millis})
    private LocalDateTime createdAt;

    public static Cafe buildElasticCafe(Cafe cafe){
        return Cafe.builder()
                .Id(cafe.getId())
                .build();
    }

}
