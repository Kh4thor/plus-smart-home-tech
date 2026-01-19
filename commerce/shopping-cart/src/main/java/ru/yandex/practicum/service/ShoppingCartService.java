package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.shopping.cart.ChangeQuantityDto;
import ru.yandex.practicum.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.exception.shopping.cart.NoProductsInShoppingCartException;
import ru.yandex.practicum.exception.shopping.store.ProductNotFoundException;
import ru.yandex.practicum.feign.warehouse.FeignClientWarehouse;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.repository.ShoppingCartRepository;
import ru.yandex.practicum.utills.ShoppingCartMapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final FeignClientWarehouse feignClientWarehouse;

    @Transactional
    public ShoppingCart getShoppingCartOrCreateNew(String username) {
        return shoppingCartRepository.findByUsername(username).orElseGet(() -> {
            log.info("Creating new shopping cart for user: {}", username);
            ShoppingCart newShoppingCart = ShoppingCart.builder()
                    .username(username)
                    .build();
            return shoppingCartRepository.save(newShoppingCart);
        });
    }

    @Transactional
    public ShoppingCart addProductsToCart(String username, Map<UUID, Integer> products) {
        String userMessage = "Unable to add shopping cart";

        // вызов корзины пользователя
        ShoppingCart shoppingCart = getShoppingCartOrCreateNew(username);

        // добавление новых товаров в корзину
        products.forEach((productId, quantity) -> shoppingCart.getProducts().merge(productId,
                quantity, Integer::sum));

        // проверка обновленной корзины на наличие товаров на складе
        ShoppingCartDto shoppingCartDto = ShoppingCartMapper.toShoppingCartDto(shoppingCart);
        BookedProductsDto bookedProductsDto = feignClientWarehouse.checkProductQuantity(shoppingCartDto);

        // валидация обновленной корзины
        validateShoppingCart(shoppingCart, username, userMessage);
        return shoppingCart;
    }

    @Transactional
    public boolean deleteShoppingCartBy(String username) {
        if (shoppingCartRepository.existsByUsername(username))
            return false;
        shoppingCartRepository.deleteByUsername(username);
        return true;
    }

    @Transactional
    public ShoppingCart removeProductsBy(String username, List<UUID> productIds) {
        String userMessage = "Unable to remove products from shopping cart";
        ShoppingCart shoppingCart = getShoppingCartOrCreateNew(username);
        validateShoppingCart(shoppingCart, username, userMessage);
        Map<UUID, Integer> products = shoppingCart.getProducts();
        productIds.forEach(products::remove);
        return shoppingCart;
    }

    @Transactional
    public ShoppingCart changeQuantity(String username, ChangeQuantityDto changeQuantityDto) {
        String userMessage = "Unable to change quantity of products for shopping cart";
        ShoppingCart shoppingCart = getShoppingCartOrCreateNew(username);
        validateShoppingCart(shoppingCart, username, userMessage);
        Map<UUID, Integer> products = shoppingCart.getProducts();
        UUID productId = changeQuantityDto.getProductId();
        Integer newQuantity = changeQuantityDto.getNewQuantity();
        if (!products.containsKey(productId)) {
            throw new ProductNotFoundException(userMessage, productId);
        }
        products.put(changeQuantityDto.getProductId(), changeQuantityDto.getNewQuantity());
        return shoppingCart;
    }

    private void validateShoppingCart(ShoppingCart shoppingCart, String username, String userMessage) {
        if (shoppingCart.getProducts() == null || shoppingCart.getProducts().isEmpty()) {
            UUID shoppingCartId = shoppingCart.getShoppingCartId();
            log.warn("{} id={} of user={}", userMessage, shoppingCartId, username);
            throw new NoProductsInShoppingCartException(userMessage, shoppingCartId, username);
        }
    }

}
