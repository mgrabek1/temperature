package com.example.temperature.exception;

public class CSVProcessingException extends RuntimeException {
    public CSVProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
