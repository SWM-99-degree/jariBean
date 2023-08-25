package com.example.jariBean.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.jariBean.dto.s3.S3ResDto.S3ImageResDto;
import com.example.jariBean.handler.ex.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpg",
            "image/jpeg",
            "image/png",
            "image/webp",
            "image/heic"
    );

    @Value("${AWS_S3_BUCKET}")
    private String BUCKET_NAME;

    @Value("${AWS_CDN_URL}")
    private String AWS_CDN_URL;

    public S3ImageResDto upload(MultipartFile imageFile) throws IOException {
        // file exception
        checkFileValidation(imageFile);

        // generate unique filename
        String uniqueFilename = generateUniqueFilename(imageFile.getOriginalFilename());

        // create metadata
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(imageFile.getSize());
        metadata.setContentType(imageFile.getContentType());

        // create object request
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, uniqueFilename, imageFile.getInputStream(), metadata);

        // request object upload
        amazonS3.putObject(putObjectRequest);

        return S3ImageResDto.builder()
                .imageUrl(AWS_CDN_URL + uniqueFilename)
                .build();
    }

    // TODO UnsupportedEncodingException.class에 해당하는 ExceptionHandler 등록!
    private String generateUniqueFilename(String fileName) throws UnsupportedEncodingException {
        return UUID.randomUUID() + URLEncoder.encode(fileName, "UTF-8");
    }

    private void checkFileValidation(MultipartFile imageFile) {
        // check file type
        if(imageFile.getContentType() == null || !ALLOWED_IMAGE_TYPES.contains(imageFile.getContentType())) {
            throw new CustomApiException(imageFile.getContentType() + " 형식의 파일은 업로드할 수 없습니다.");
        }
    }
}
