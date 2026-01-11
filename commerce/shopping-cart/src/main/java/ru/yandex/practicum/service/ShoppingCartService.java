package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.exception.ShoppingCartNotFoundException;
import ru.yandex.practicum.model.ShoppingCart;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartService {

    ShoppingCartRepository shoppingCartRepository;

    @Transactional
    public ShoppingCart getShoppingCart(String username) {
        String userMessage = "Unable to get shopping cart";
        return shoppingCartRepository.findByUsername(username).orElseThrow(() -> {
            log.warn("{} by username={}", userMessage, username);
            return new ShoppingCartNotFoundException(userMessage, username);
        });
    }

    @Transactional
    public ShoppingCart save(ShoppingCartDto shoppingCartDto, String username) {
        ShoppingCart shoppingCart = ShoppingCartMapper.toShoppingCart(shoppingCartDto, username);
        return shoppingCartRepository.save(shoppingCart);
    }

    @Transactional
    public boolean deleteShoppingCartBy(String username) {
        if (shoppingCartRepository.existsByUsername(username))
            return false;
        shoppingCartRepository.deleteByUsername(username);
        return true;
    }

    @Transactional
    public ShoppingCart removeShoppingCartBy(String username, List<UUID> productIds) {
        ShoppingCart shoppingCart = getShoppingCart(username);
        Map<UUID, Integer> products = shoppingCart.getProducts();
        productIds.forEach(products::remove);
        return shoppingCart;
    }

    @Transactional
    public
}
