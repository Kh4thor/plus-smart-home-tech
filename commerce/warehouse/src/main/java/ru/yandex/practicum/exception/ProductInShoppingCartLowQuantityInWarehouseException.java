package ru.yandex.practicum.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ProductInShoppingCartLowQuantityInWarehouseException extends RuntimeException {

    private final String userMessage;

    public ProductInShoppingCartLowQuantityInWarehouseException(String userMessage, UUID productId) {
        super("Product in shopping cart low quantity in warehouse, product id=" + productId);
        this.userMessage = userMessage;
    }
}
