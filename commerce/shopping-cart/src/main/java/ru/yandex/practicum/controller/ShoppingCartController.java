package ru.yandex.practicum.controller;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.shopping.cart.ChangeQuantityDto;
import ru.yandex.practicum.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.service.ShoppingCartService;
import ru.yandex.practicum.utills.ShoppingCartMapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Контроллер для управления корзиной покупок пользователей.
 * Предоставляет REST API для операций с корзиной: получение, сохранение,
 * удаление, изменение содержимого и количества товаров.
 *
 * <p>Все методы требуют указания имени пользователя (username)
 * в качестве параметра запроса для идентификации корзины.</p>
 *
 * <p>Адрес endpoint: {@code /api/v1/shopping-cart}</p>
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController {

    /**
     * Сервис для бизнес-логики работы с корзиной покупок.
     */
   private final ShoppingCartService shoppingCartService;

    /**
     * Получает корзину покупок для указанного пользователя.
     *
     * @param username имя пользователя, чью корзину нужно получить
     * @return DTO корзины покупок с товарами пользователя
     * @throws ConstraintViolationException если username пустой или null
     * @see ShoppingCart
     * @see ShoppingCartDto
     * @see ShoppingCartService#getShoppingCartOrCreateNew(String)
     */
    @GetMapping
    public ShoppingCartDto getShoppingCart(@RequestParam @NotBlank String username) {
        ShoppingCart retrieved = shoppingCartService.getShoppingCartOrCreateNew(username.trim());
        return ShoppingCartMapper.toShoppingCartDto(retrieved);
    }

    /**
     * Сохраняет или обновляет корзину покупок для указанного пользователя.
     * Заменяет текущее содержимое корзины на переданное в DTO.
     *
     * @param username имя пользователя, чью корзину нужно обновить
     * @param products словарь с новым содержимым корзины (UUID товара и его количество)
     * @return DTO сохранённой корзины покупок
     * @throws ConstraintViolationException если username пустой или null,
     *                                                         или shoppingCartDto не проходит валидацию
     * @see ShoppingCartService#addProductsToCart(String, Map)
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto addProductsToCart(
            @RequestParam @NotBlank String username,
            @RequestBody @NotEmpty Map<UUID, @NotNull @Positive Integer> products
    ) {
        ShoppingCart saved = shoppingCartService.addProductsToCart(username.trim(), products);
        return ShoppingCartMapper.toShoppingCartDto(saved);
    }

    /**
     * Удаляет корзину покупок для указанного пользователя.
     *
     * @param username имя пользователя, чью корзину нужно удалить
     * @return {@code true} если корзина была успешно удалена,
     * {@code false} если корзина не существовала
     * @throws ConstraintViolationException если username пустой или null
     * @see ShoppingCartService#deleteShoppingCartBy(String)
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean deleteShoppingCart(@RequestParam @NotBlank String username) {
        return shoppingCartService.deleteShoppingCartBy(username.trim());
    }

    /**
     * Удаляет указанные товары из корзины пользователя.
     *
     * @param username   имя пользователя, из чьей корзины нужно удалить товары
     * @param productIds список идентификаторов товаров для удаления
     * @return DTO обновлённой корзины покупок после удаления товаров
     * @throws ConstraintViolationException если username пустой или null
     * @see ShoppingCartService#removeProductsBy(String, List)
     */
    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto removeProducts(
            @RequestParam @NotBlank String username,
            @RequestBody @Valid List<UUID> productIds) {
        ShoppingCart shoppingCart = shoppingCartService.removeProductsBy(username.trim(), productIds);
        return ShoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    /**
     * Изменяет количество конкретного товара в корзине пользователя.
     *
     * <p>Метод позволяет увеличить или уменьшить количество товара в корзине.
     * Для полного удаления товара используйте {@link #changeQuantity(String, List)}.</p>
     *
     * @param username          имя пользователя, в чьей корзине нужно изменить количество
     * @param changeQuantityDto DTO с информацией об изменении количества товара
     * @return DTO обновлённой корзины покупок после изменения количества
     * @throws ConstraintViolationException если username пустой или null,
     *                                                         или changeQuantityDto не проходит валидацию
     * @see ShoppingCartService#changeQuantity(String, ChangeQuantityDto)
     * @see #changeQuantity(String, List)
     */
    @PostMapping("change-quantity")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto changeQuantity(
            @RequestParam @NotBlank String username,
            @RequestBody @Valid ChangeQuantityDto changeQuantityDto) {
        ShoppingCart shoppingCart = shoppingCartService.changeQuantity(username.trim(), changeQuantityDto);
        return ShoppingCartMapper.toShoppingCartDto(shoppingCart);
    }
}