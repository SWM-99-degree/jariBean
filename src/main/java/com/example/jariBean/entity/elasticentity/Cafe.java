package com.example.jariBean.entity.elasticentity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "cafe")
public class Cafe {

    @Id
    @Field(type = FieldType.Text)
    private String Id;

    @Field(type = FieldType.Text)
    private String text;

    @Field(type = FieldType.Auto)
    private GeoPoint coordinate;


}
