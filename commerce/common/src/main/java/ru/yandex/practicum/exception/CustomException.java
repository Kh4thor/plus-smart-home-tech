package ru.yandex.practicum.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private String userMessage;

    public CustomException(String errorMessage, String userMessage) {
        super(errorMessage);
    }
}
