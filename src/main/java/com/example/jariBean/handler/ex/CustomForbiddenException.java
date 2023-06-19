package com.example.jariBean.handler.ex;

public class CustomForbiddenException extends RuntimeException{
    public CustomForbiddenException(String message) {
        super(message);
    }
}
