package ru.yandex.practicum.exception;


import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends RuntimeException {
    private Cause cause;
    private StackTrace stackTrace;
    private HttpStatus httpStatus;
    private String userMessage;
    private String message;
    private Suppressed suppressed;
}
