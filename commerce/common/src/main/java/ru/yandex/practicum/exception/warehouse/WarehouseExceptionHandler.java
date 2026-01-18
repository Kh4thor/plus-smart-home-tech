package ru.yandex.practicum.exception.warehouse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exception.ErrorResponse;
import ru.yandex.practicum.exception.CustomException;

@Slf4j
@RestControllerAdvice
public class WarehouseExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoSpecifiedProductInWarehouseException(
            NoSpecifiedProductInWarehouseException exception) {
        return handleException(exception);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleProductInShoppingCartLowQuantityInWarehouseException(
            ProductInShoppingCartLowQuantityInWarehouseException exception) {
        return handleException(exception);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSpecifiedProductAlreadyInWarehouseException(
            SpecifiedProductAlreadyInWarehouseException exception) {
        return handleException(exception);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleWarehouseProductNotFoundException(
            WarehouseProductNotFoundException exception) {
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
