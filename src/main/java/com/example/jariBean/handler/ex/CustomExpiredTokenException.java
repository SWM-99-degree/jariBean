package com.example.jariBean.handler.ex;

public class CustomExpiredTokenException extends RuntimeException{
    public CustomExpiredTokenException(String message) {
        super(message);
    }
}
