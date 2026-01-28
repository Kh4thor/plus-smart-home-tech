package ru.yandex.practicum.exception.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exception.CustomException;
import ru.yandex.practicum.exception.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class PaymentExceptionHandler {

    @ExceptionHandler
    public ErrorResponse handleNotEnoughInfoInOrderToCalculateException(NotEnoughInfoInOrderToCalculateException exception) {
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
