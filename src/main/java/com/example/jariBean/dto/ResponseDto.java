package com.example.jariBean.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseDto<T> {
    private final Integer code; // 1: 성공 2: 실패
    private final String msg;
    private final T data;
}
