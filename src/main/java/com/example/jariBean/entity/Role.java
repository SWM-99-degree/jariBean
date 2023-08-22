package com.example.jariBean.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN("관리자"), CUSTOMER("고객"), MANAGER("매니저"), UNREGISTERED("미등록");
    private String role;
}
