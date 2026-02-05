package ru.yandex.practicum.feign.shopping.cart;

import feign.FeignException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.shopping.cart.ChangeQuantityDto;
import ru.yandex.practicum.dto.shopping.cart.ShoppingCartDto;

import java.util.List;
import java.util.UUID;

/**
 * Feign-клиент для взаимодействия с сервисом корзины покупок (shopping cart service).
 * Предоставляет методы для управления корзиной покупок: создание, обновление,
 * удаление товаров и изменение их количества.
 * <p>
 * Все методы соответствуют REST API эндпоинтам сервиса корзины покупок.
 * Используется аннотация {@link FeignClient} для интеграции через Spring Cloud OpenFeign.
 * Интерфейс валидируется с помощью аннотации {@link Validated}.
 * </p>
 */
@Validated
@FeignClient(name = "shopping-cart",
        contextId = "shopping-cartApiClient",
        path = "/api/v1/shopping-cart")
public interface FeignClientShoppingCart {

    /**
     * Создает или обновляет корзину покупок для указанного пользователя.
     *
     * @param shoppingCartDto объект {@link ShoppingCartDto} с данными корзины покупок
     * @param username        имя пользователя, для которого создается или обновляется корзина
     * @return {@link ShoppingCartDto} обновленной или созданной корзины покупок
     * @throws FeignException в случае ошибки взаимодействия с сервисом
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    ShoppingCartDto putShoppingCart(
            @RequestBody @Valid ShoppingCartDto shoppingCartDto,
            @RequestParam @NotBlank String username) throws FeignException;

    /**
     * Удаляет корзину покупок для указанного пользователя.
     *
     * @param username имя пользователя, для которого удаляется корзина
     * @return true, если корзина успешно удалена, false в противном случае
     * @throws FeignException в случае ошибки взаимодействия с сервисом
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    boolean deleteShoppingCart(@RequestParam @NotBlank String username) throws FeignException;

    /**
     * Удаляет указанные товары из корзины покупок пользователя.
     *
     * @param username   имя пользователя, из корзины которого удаляются товары
     * @param productIds список идентификаторов товаров для удаления
     * @return {@link ShoppingCartDto} корзины покупок после удаления товаров
     * @throws FeignException в случае ошибки взаимодействия с сервисом
     */
    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    ShoppingCartDto removeShoppingCart(
            @RequestParam @NotBlank String username,
            @RequestBody List<UUID> productIds) throws FeignException;

    /**
     * Изменяет количество указанного товара в корзине покупок пользователя.
     *
     * @param username          имя пользователя, в корзине которого изменяется количество товара
     * @param changeQuantityDto объект {@link ChangeQuantityDto} с данными об изменении количества
     * @return {@link ShoppingCartDto} корзины покупок после изменения количества товара
     * @throws FeignException в случае ошибки взаимодействия с сервисом
     */
    @PostMapping("change-quantity")
    @ResponseStatus(HttpStatus.OK)
    ShoppingCartDto changeQuantity(
            @RequestParam @NotBlank String username,
            @RequestBody ChangeQuantityDto changeQuantityDto) throws FeignException;
}