package com.example.jariBean.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Document
@Getter
@Setter
@NoArgsConstructor
public class TableClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tableClassId")
    private String id;

    private String cafeId;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    private Integer seating;

    @Column(nullable = false)
    private List<TableOption> tableOptionList;

    @DBRef
    private Cafe cafe;

    @CreatedDate
    @Column(updatable = false) // 생성일자(createdDate)에 대한 정보는 생성시에만 할당 가능, 갱신 불가
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Version //
    private Integer version;

    public enum TableOption {
        PLUG, HEIGHT, RECTANGLE, BACKREST
    }

    @Builder
    public TableClass(String id, String cafeId, String name, Integer seating, List<TableOption> tableOptionList) {
        this.id = id;
        this.cafeId = cafeId;
        this.name = name;
        this.seating = seating;
        this.tableOptionList = tableOptionList;
    }

    public void update(String name, Integer seating, List<TableOption> option) {
        this.name = name;
        this.seating = seating;
        this.tableOptionList = option;
    }
}
