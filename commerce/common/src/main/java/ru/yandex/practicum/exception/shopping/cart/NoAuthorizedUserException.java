package ru.yandex.practicum.exception.shopping.cart;

import lombok.Getter;
import ru.yandex.practicum.exception.CustomException;

@Getter
public class NoAuthorizedUserException extends CustomException {

    public NoAuthorizedUserException(String userMessage, String username) {
        super("User " + username + " no authorized", userMessage);
    }
}
