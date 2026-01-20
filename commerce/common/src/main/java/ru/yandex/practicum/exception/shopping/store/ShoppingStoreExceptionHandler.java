package ru.yandex.practicum.exception.shopping.store;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exception.ErrorResponse;


@Slf4j
@RestControllerAdvice
public class ShoppingStoreExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleProductNotFoundException(ProductNotFoundException exception) {
        log.warn(exception.getUserMessage());
        log.warn(exception.getMessage());
        return ErrorResponse.builder()
                .userMessage(exception.getUserMessage())
                .message(exception.getMessage())
                .build();
    }

}
