package com.example.jariBean.dto.firebasedto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FCMReqDto {
    private String userId;
    private String title;
    private String body;

    @Builder
    public FCMReqDto(String userId, String title, String body){
        this.userId = userId;
        this.title = title;
        this.body = body;
    }
}
