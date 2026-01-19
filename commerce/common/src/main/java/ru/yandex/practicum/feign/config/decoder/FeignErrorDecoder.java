package ru.yandex.practicum.feign.config.decoder;

import feign.Response;
import feign.codec.ErrorDecoder;
import ru.yandex.practicum.feign.config.exception.InternalServerErrorException;
import ru.yandex.practicum.feign.config.exception.NotFoundException;

public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 404 -> {
                return new NotFoundException("Метод: " + methodKey + " не найден");
            }
            case 500 -> {
                return new InternalServerErrorException("Ошибка на стороне сервера");
            }
            default -> {
                return defaultDecoder.decode(methodKey, response);
            }
        }
    }
}