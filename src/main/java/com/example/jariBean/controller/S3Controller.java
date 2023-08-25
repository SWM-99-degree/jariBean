package com.example.jariBean.controller;

import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.dto.s3.S3ResDto.S3ImageResDto;
import com.example.jariBean.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity imageUpload(@RequestPart MultipartFile imageFile) throws IOException {
        S3ImageResDto imageResDto = s3Service.upload(imageFile);
        return new ResponseEntity<>(new ResponseDto<>(1, "이미지 업로드에 성공하였습니다.", imageResDto), OK);
    }

}
