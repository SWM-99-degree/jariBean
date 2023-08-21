package com.example.jariBean.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${AWS_S3_BUCKET}")
    private String BUCKET_NAME;

    public String generatePreSignedUrl(String file) {
        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(BUCKET_NAME, generateObjectKey(file))
                .withMethod(HttpMethod.PUT)
                .withExpiration(new Date(new Date().getTime() + (60 * 1000)));
        return amazonS3Client.generatePresignedUrl(req).toString();
    }

    public String generateObjectKey(String file) {
        String[] fileStructure = file.split("\\.");
        return fileStructure[0] + "-" + LocalDateTime.now() + "." + fileStructure[1];
    }

}
