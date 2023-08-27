package com.example.jariBean.dto.s3;

import lombok.Builder;
import lombok.Data;

public class S3ResDto {

    @Data
    @Builder
    public static class S3ImageResDto {
        private String imageUrl;
    }

}
