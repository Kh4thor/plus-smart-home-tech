package ru.yandex.practicum.exception.shopping.cart;

import ru.yandex.practicum.exception.CustomException;

import java.util.UUID;

public class NoProductsInShoppingCartException extends CustomException {

    public NoProductsInShoppingCartException(String userMessage, UUID shoppingCartId, String username) {
        super("No products in shopping cart id=" + shoppingCartId + " of user: " + username, userMessage);
    }
}
