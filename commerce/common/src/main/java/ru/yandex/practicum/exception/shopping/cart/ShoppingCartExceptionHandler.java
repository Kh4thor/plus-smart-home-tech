package ru.yandex.practicum.exception.shopping.cart;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exception.CustomException;
import ru.yandex.practicum.exception.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ShoppingCartExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleShoppingCartNotFoundException(ShoppingCartNotFoundException exception) {
        return handleException(exception);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoAuthorizedUserException(NoAuthorizedUserException exception) {
        return handleException(exception);
    }


    private ErrorResponse handleException(CustomException exception) {
        log.warn("{} {}", exception.getUserMessage(), exception.getMessage());
        return ErrorResponse.builder()
                .userMessage(exception.getUserMessage())
                .message(exception.getMessage())
                .build();
    }
}
