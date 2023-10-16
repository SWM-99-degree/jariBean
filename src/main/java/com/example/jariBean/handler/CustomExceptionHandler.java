package com.example.jariBean.handler;

import com.example.jariBean.dto.ResponseDto;
import com.example.jariBean.handler.ex.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.MethodNotSupportedException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.WebClientResponseException.InternalServerError;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // handling exceptions for no DB connect
    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<?> apiException(ConnectException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), "DB 연결에 오류가 발생하였습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // handling exceptions for disallowed methods
    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<?> apiException(MethodNotAllowedException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), "해당 Method는 허용되지 않습니다."), HttpStatus.METHOD_NOT_ALLOWED);
    }


    // handling exceptions for not allowed
    @ExceptionHandler(MethodNotSupportedException.class)
    public ResponseEntity<?> apiException(MethodNotSupportedException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), "URL과 Method가 일치하지 않습니다."), HttpStatus.METHOD_NOT_ALLOWED);
    }

    // handling exceptions for not found
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> apiException(NoHandlerFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), "URL에 해당하는 Controller가 없습니다."), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity sizeLimitExceededException(SizeLimitExceededException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), "업로드가 가능한 파일의 용량은 최대 10MB입니다."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnsupportedEncodingException.class)
    public ResponseEntity unsupportedEncodingException(UnsupportedEncodingException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), "filename의 URL encode에 문제가 발생하였습니다. "), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity webClientResponseException(WebClientResponseException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), "외부 API 요청에서 문제가 발생하였습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity internalServerError(InternalServerError e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), "서버 처리 과정에서 문제가 발생하였습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity nullException(NullPointerException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), "해당 되는 데이터가 없습니다. 다른 요청을 진행해주세요."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());

        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> errorMap = new HashMap<>();

        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError error : fieldErrors) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
        }

        return new ResponseEntity<>(new ResponseDto<>(-1, "데이터 유효성 검증에 실패하였습니다.", errorMap), HttpStatus.BAD_REQUEST);
    }

}
