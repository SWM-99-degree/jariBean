package com.example.jariBean.handler.ex;

public class CustomApiException extends RuntimeException{
    public CustomApiException(String message) {
        super(message);
    }
}
