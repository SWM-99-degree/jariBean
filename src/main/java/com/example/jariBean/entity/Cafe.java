package com.example.jariBean.entity;


import lombok.*;
import net.bytebuddy.asm.Advice;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Getter
@Setter
@NoArgsConstructor
public class Cafe {

    @Id
    private String id;

    @Column(unique = true, nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 11)
    private String phoneNumber;

    private String image;

    private String instagram;

    @Column(nullable = false, length = 60)
    private String address;

    @Column(length = 200)
    private String description;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint coordinate;

    @Column(nullable = false)
    private boolean matching;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;



    @CreatedDate
    @Column(updatable = false) // 생성일자(createdDate)에 대한 정보는 생성시에만 할당 가능, 갱신 불가
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Version //
    private Integer version;

    @DBRef
    private List<CafeOperatingTime> cafeOperatingTimeList;

    @Builder
    public Cafe(String id, String name, String phoneNumber, String image, String address, String description, GeoJsonPoint coordinate) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.image = image;
        this.address = address;
        this.description = description;
        this.coordinate = coordinate;
        this.matching = true;
    }

    public void updateCafeName(String name) {
        this.name = name;
    }

    public boolean toggleMatchingStatus() {
        matching = !matching;
        return matching;
    }

}