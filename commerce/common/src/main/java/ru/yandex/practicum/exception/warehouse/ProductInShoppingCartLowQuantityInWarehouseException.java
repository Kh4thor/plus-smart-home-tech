package ru.yandex.practicum.exception.warehouse;

import lombok.Getter;
import ru.yandex.practicum.exception.CustomException;

import java.util.UUID;

@Getter
public class ProductInShoppingCartLowQuantityInWarehouseException extends CustomException {

    public ProductInShoppingCartLowQuantityInWarehouseException(String userMessage, UUID productId) {
        super("Product in shopping cart low quantity in warehouse, product id=" + productId, userMessage);
    }
}
