package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ChangeQuantityDto;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.service.ShoppingCartMapper;
import ru.yandex.practicum.service.ShoppingCartService;

import java.util.List;
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
    ShoppingCartService shoppingCartService;

    /**
     * Получает корзину покупок для указанного пользователя.
     *
     * @param username имя пользователя, чью корзину нужно получить
     * @return DTO корзины покупок с товарами пользователя
     * @see ShoppingCart
     * @see ShoppingCartDto
     * @throws jakarta.validation.ConstraintViolationException если username пустой или null
     * @see ShoppingCartService#getShoppingCart(String)
     */
    @GetMapping
    public ShoppingCartDto getShoppingCart(@RequestParam @NotBlank String username) {
        ShoppingCart retrieved = shoppingCartService.getShoppingCart(username.trim());
        return ShoppingCartMapper.toShoppingCartDto(retrieved);
    }

    /**
     * Сохраняет или обновляет корзину покупок для указанного пользователя.
     * Заменяет текущее содержимое корзины на переданное в DTO.
     *
     * @param shoppingCartDto DTO с новым содержимым корзины
     * @param username        имя пользователя, чью корзину нужно обновить
     * @return DTO сохранённой корзины покупок
     * @throws jakarta.validation.ConstraintViolationException если username пустой или null,
     *                                                         или shoppingCartDto не проходит валидацию
     * @see ShoppingCartService#save(ShoppingCartDto, String)
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto putShoppingCart(
            @RequestBody @Valid ShoppingCartDto shoppingCartDto,
            @RequestParam @NotBlank String username) {
        ShoppingCart saved = shoppingCartService.save(shoppingCartDto, username.trim());
        return ShoppingCartMapper.toShoppingCartDto(saved);
    }

    /**
     * Удаляет корзину покупок для указанного пользователя.
     *
     * @param username имя пользователя, чью корзину нужно удалить
     * @return {@code true} если корзина была успешно удалена,
     * {@code false} если корзина не существовала
     * @throws jakarta.validation.ConstraintViolationException если username пустой или null
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
     * @throws jakarta.validation.ConstraintViolationException если username пустой или null
     * @see ShoppingCartService#removeShoppingCartBy(String, List)
     */
    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto removeShoppingCart(
            @RequestParam @NotBlank String username,
            @RequestBody List<UUID> productIds) {
        ShoppingCart shoppingCart = shoppingCartService.removeShoppingCartBy(username.trim(), productIds);
        return ShoppingCartMapper.toShoppingCartDto(shoppingCart);
    }

    /**
     * Изменяет количество конкретного товара в корзине пользователя.
     *
     * <p>Метод позволяет увеличить или уменьшить количество товара в корзине.
     * Для полного удаления товара используйте {@link #removeShoppingCart(String, List)}.</p>
     *
     * @param username          имя пользователя, в чьей корзине нужно изменить количество
     * @param changeQuantityDto DTO с информацией об изменении количества товара
     * @return DTO обновлённой корзины покупок после изменения количества
     * @throws jakarta.validation.ConstraintViolationException если username пустой или null,
     *                                                         или changeQuantityDto не проходит валидацию
     * @see ShoppingCartService#changeQuantity(String, ChangeQuantityDto)
     * @see #removeShoppingCart(String, List)
     */
    @PostMapping("change-quantity")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto changeQuantity(
            @RequestParam @NotBlank String username,
            @RequestBody ChangeQuantityDto changeQuantityDto) {
        ShoppingCart shoppingCart = shoppingCartService.changeQuantity(username.trim(), changeQuantityDto);
        return ShoppingCartMapper.toShoppingCartDto(shoppingCart);
    }
}