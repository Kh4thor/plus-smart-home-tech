package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.ShoppingCart;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartService {

    ShoppingCartRepository shoppingCartRepository;

    public ShoppingCart getShoppingCart(String userName) {
        return shoppingCartRepository.findByShoppingCartName();
    }
}
