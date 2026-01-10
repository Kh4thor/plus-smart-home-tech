package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.ShoppingCartNotFoundException;
import ru.yandex.practicum.model.ShoppingCart;

import java.util.Optional;

@Slf4j
@Service
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartService {

    ShoppingCartRepository shoppingCartRepository;

    public ShoppingCart getShoppingCart(String username) {
        String userMessage = "Unable to get shopping cart";
        Optional<ShoppingCart> optionalCart = shoppingCartRepository.findByUsername(username);
        optionalCart.orElseThrow(() -> new ShoppingCartNotFoundException(userMessage, username));
        return shoppingCartRepository.findByUsername(username).orElseThrow(() -> {
            log.warn("{} by username={}", userMessage, username);
            return new ShoppingCartNotFoundException(userMessage, username);
        });
    }
}
