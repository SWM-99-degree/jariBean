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
    private String userId;

    @Column(nullable = false)
    private String cafeId;

    @Column(nullable = false)
    private String tableId;

    @Column(nullable = false)
    private LocalDateTime reservedStartTime;

    @Column(nullable = false)
    private LocalDateTime reservedEndTime;

    @Column(nullable = false)
    private ReservedStatus reservedStatus;

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
    public Reserved(String userId, String cafeId, String tableId, LocalDateTime reservedStartTime, LocalDateTime reservedEndTime) {
        this.userId = userId;
        this.cafeId = cafeId;
        this.tableId = tableId;
        this.reservedEndTime = reservedEndTime;
        this.reservedStartTime = reservedStartTime;
        this.reservedStatus = ReservedStatus.VALID;
    }
}
