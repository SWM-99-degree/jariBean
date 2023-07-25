package com.example.jariBean.entity.elasticentity;


import lombok.*;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "cafe.cafe")
public class Cafe {

    @Id
    private String Id;

    private String name;

    private String phoneNumber;

    private String imageUrl;

    private String address;

    private String description;

    private GeoPoint coordinate;

    @Field(type = FieldType.Date, format = {DateFormat.date_hour_minute_second_millis, DateFormat.epoch_millis})
    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;


}
