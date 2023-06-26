package com.example.jariBean.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.time.LocalDateTime;

@Document
@Getter
@NoArgsConstructor
public class Cafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafeId")
    private String id;

    @Column(unique = true, nullable = false, length = 20)
    private String cafeName;

    @Column(nullable = false, length = 11)
    private String cafePhoneNumber;

    @Column(nullable = false, length = 60) // (Bcrypt)
    private String cafePassword;

    @Column(nullable = false, length = 60)
    private String cafeAddress;

    // 수정한 부분 충돌!!
    @Column(nullable = false)
    private GeoJsonPoint coordinate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private User.UserRole userRole;

    @CreatedDate
    @Column(updatable = false) // 생성일자(createdDate)에 대한 정보는 생성시에만 할당 가능, 갱신 불가
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Version //
    private Integer version;

    // TODO userRole, UserType

    @Getter
    @AllArgsConstructor
    public enum UserRole {
        ADMIN("관리자"), CUSTOMER("고객"), MANAGER("매니저");
        private String role;
    }

    @Builder
    public Cafe(String id, String cafeName, String cafePhoneNumber, String cafePassword, String cafeAddress, User.UserRole userRole, GeoJsonPoint coordinate) {
        this.id = id;
        this.cafeName = cafeName;
        this.cafePhoneNumber = cafePhoneNumber;
        this.cafePassword = cafePassword;
        this.cafeAddress = cafeAddress;
        this.userRole = userRole;
        this.coordinate = coordinate;
    }

}