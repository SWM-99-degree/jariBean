package com.example.jariBean.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class Table {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tableId")
    private String id;

    @Column(nullable = false)
    private String cafeId;

    @Column(nullable = false)
    private String tableClassId;

    @Column(nullable = false, length = 100)
    private String name; // 사장님이 알아볼 수 있는 이름 ex. 창가자리

    @Column(nullable = false, length = 100)
    private String description; // 사용자에게 보이는 이름 ex.스터디용 1-1

    @Column(nullable = false)
    private Integer seating; // 인원 수

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private List<TableClass.TableOption> tableOptionList;

    @DBRef
    private Cafe cafe;

    @DBRef
    private TableClass tableClass;

    @DBRef
    private List<Reserved> reservedList;

    @CreatedDate
    @Column(updatable = false) // 생성일자(createdDate)에 대한 정보는 생성시에만 할당 가능, 갱신 불가
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Version //
    private Integer version;
}
