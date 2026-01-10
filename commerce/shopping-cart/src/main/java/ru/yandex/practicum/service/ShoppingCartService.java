package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.ShoppingCart;

@Slf4j
@Service
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartService {

    ShoppingCartRepository shoppingCartRepository;

    public ShoppingCart getShoppingCart(String username) {
        String userMessage = "Unable to retrieve shopping cart for username: " + username;
        return shoppingCartRepository.findByShoppingCartName(username).orElseThrow({
                log.warn();
        })
    }
}
