package com.example.jariBean.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Document
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private String id;

    @Column(unique = true, nullable = false, length = 20)
    private String userName;

    @Column(nullable = false, length = 11)
    private String userPhoneNumber;

    @Column(nullable = false, length = 60) // (Bcrypt)
    private String userPassword;

    @Column(nullable = false, length = 20)
    private String userNickname;

    @Enumerated(STRING)
    @Column(nullable = false)
    private UserRole userRole;

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

    @Getter
    @AllArgsConstructor
    public enum UserType {
        NORMAL("기본"), KAKAO("카카오");
        private String role;
    }

    @Builder
    public User(String id, String userName, String userPhoneNumber, String userPassword, String userNickname, UserRole userRole) {
        this.id = id;
        this.userName = userName;
        this.userPhoneNumber = userPhoneNumber;
        this.userPassword = userPassword;
        this.userNickname = userNickname;
        this.userRole = userRole;
    }


}
