package com.example.mongoRedis.exception;

public class CustomServiceException extends RuntimeException {
    public CustomServiceException(String message) {
        super(message);
    }
}
