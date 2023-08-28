package com.example.jariBean.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Document
@Getter
@NoArgsConstructor
public class CafeManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String email;

    private String password;

    private String cafeId;

    private Role role;

    @Builder
    public CafeManager(String id, String email, String password, String cafeId, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.cafeId = cafeId;
        this.role = role;
    }

}
