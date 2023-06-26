package com.example.jariBean.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Getter
@NoArgsConstructor
public class TableClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tableClassId")
    private String id;

    private String cafeId;

    @Column(nullable = false, length = 20)
    private String tableClassName;

    @Column(nullable = false)
    private List<TableOption> tableOptions;

    @CreatedDate
    @Column(updatable = false) // 생성일자(createdDate)에 대한 정보는 생성시에만 할당 가능, 갱신 불가
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Version //
    private Integer version;

    enum TableOption {
        PLUG, HEIGHT, RECTANGLE, BACKREST
    }
}
