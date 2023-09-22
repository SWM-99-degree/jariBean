package com.example.jariBean.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.time.LocalDateTime;

@Document
@Getter
@NoArgsConstructor
public class Reserved {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservedId")
    private String id;

    @Column(nullable = false)
    private User user;

    @Column(nullable = false)
    private Cafe cafe;

    @Column(nullable = false)
    private Table table;

    @Column(nullable = false)
    private TableClass tableClass;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private ReservedStatus status;

    @CreatedDate
    @Column(updatable = false) // 생성일자(createdDate)에 대한 정보는 생성시에만 할당 가능, 갱신 불가
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Version //
    private Integer version;


    public enum ReservedStatus {
        VALID, CANCEL
    }

    @Builder
    public Reserved(String id, User user, Cafe cafe, Table table, TableClass tableClass, LocalDateTime startTime, LocalDateTime endTime, ReservedStatus status) {
        this.id = id;
        this.user = user;
        this.cafe = cafe;
        this.table = table;
        this.tableClass = tableClass;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }
}
