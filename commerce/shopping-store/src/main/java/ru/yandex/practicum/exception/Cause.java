package ru.yandex.practicum.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
public class Cause {
    private StackTrace stackTrace;
    private String message;
    private String localizedMessage;
}
