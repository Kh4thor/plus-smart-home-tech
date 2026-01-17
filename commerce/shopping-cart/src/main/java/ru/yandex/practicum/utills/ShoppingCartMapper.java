package ru.yandex.practicum.utills;

import ru.yandex.practicum.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.model.ShoppingCart;

public class ShoppingCartMapper {

    public static ShoppingCartDto toShoppingCartDto(ShoppingCart shoppingCart) {
        if (shoppingCart == null) return null;
        return ShoppingCartDto.builder()
                .shoppingCartId(shoppingCart.getShoppingCartId())
                .products(shoppingCart.getProducts())
                .build();
    }

    public static ShoppingCart toShoppingCart(ShoppingCartDto shoppingCartDto, String username) {
        return ShoppingCart.builder()
                .username(username)
                .products(shoppingCartDto.getProducts())
                .build();
    }
}
