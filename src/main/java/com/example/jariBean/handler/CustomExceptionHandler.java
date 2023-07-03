package com.example.jariBean.handler;

import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.handler.ex.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(CustomNoContentException.class)
    public ResponseEntity<?> apiException(CustomNoContentException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(CustomDBException.class)
    public ResponseEntity<?> apiException(CustomDBException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> apiException(CustomApiException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomForbiddenException.class)
    public ResponseEntity<?> forbiddenException(CustomForbiddenException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<?> validationApiException(CustomValidationException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomExpiredTokenException.class)
    public ResponseEntity<?> expiredTokenException(CustomExpiredTokenException e) {
        log.error(e.getMessage());

        // TODO 즉각적으로 프론트에서 토큰을 보내서 해당 URL로 가게 하는 방법이 있나?
        // 없다면, 프론트에서 /api/users/refreshtoken으로 가게 하는 url로 가도록 하는 페이지 생성
        // refreshToken을 통해서 전화번호를 알아내고, 여기에 들어가 있는지 확인하기
        String redirectUrl = "/api/users/refreshtoken";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", redirectUrl);

        return new ResponseEntity<>(httpHeaders, HttpStatus.PERMANENT_REDIRECT);
    }
}
