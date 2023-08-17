package com.example.jariBean.controller;

import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class S3Controller {

    private final S3Service s3Service;

    @GetMapping("/upload")
    public ResponseEntity generatePreSignedUrl(@RequestParam("fileName") String fileName) {
        String preSignedUrl = s3Service.generatePreSignedUrl(fileName);
        return new ResponseEntity<>(new ResponseDto<>(1, "S3 pre-signed url 발급에 성공하였습니다.", preSignedUrl), CREATED);
    }

}
