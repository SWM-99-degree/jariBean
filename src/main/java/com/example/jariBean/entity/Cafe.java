package com.example.jariBean.entity;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(unique = true, nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 11)
    private String phoneNumber;

    private String imageUrl;

    private String instagramUrl;

    @Column(nullable = false, length = 60)
    private String address;

    @Column(length = 200)
    private String description;

    @Column
    private String cafeImg;

    // 수정한 부분 충돌!!
    @Column(nullable = false)
    private GeoJsonPoint coordinate;

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
    public Cafe(String id, String name, String phoneNumber, String imageUrl, String address, String description, GeoJsonPoint coordinate) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
        this.address = address;
        this.description = description;
        this.coordinate = coordinate;
    }

    public void updateCafeName(String name) {
        this.name = name;
    }

}