package ru.yandex.practicum.feign.shopping.store;

import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.shopping.store.ProductDto;
import ru.yandex.practicum.dto.shopping.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.enums.shopping.store.ProductCategory;

import java.util.List;
import java.util.UUID;

/**
 * Feign-клиент для взаимодействия с сервисом магазина (shopping store service).
 * Предоставляет методы для управления товарами в магазине: поиск, создание,
 * обновление, удаление товаров и управление их количеством.
 * <p>
 * Все методы соответствуют REST API эндпоинтам сервиса магазина.
 * Используется аннотация {@link FeignClient} для интеграции через Spring Cloud OpenFeign.
 * Интерфейс валидируется с помощью аннотации {@link Validated}.
 * </p>
 */
@Validated
@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface FeignClientShoppingStore {

    /**
     * Находит товары по указанным категориям с поддержкой пагинации и сортировки.
     *
     * @param categories список категорий товаров для фильтрации
     * @param page номер страницы для пагинации (по умолчанию 0)
     * @param size количество товаров на странице (по умолчанию 20)
     * @param sort параметр сортировки (необязательный)
     * @return список {@link ProductDto} товаров, соответствующих указанным категориям
     * @throws FeignException в случае ошибки взаимодействия с сервисом
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<ProductDto> findByCategories(
            @RequestParam List<ProductCategory> categories,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sort) throws FeignException;

    /**
     * Создает новый товар в магазине.
     *
     * @param productDto объект {@link ProductDto} с данными нового товара
     * @return {@link ProductDto} созданного товара
     * @throws FeignException в случае ошибки взаимодействия с сервисом
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    ProductDto create(@RequestBody @Valid ProductDto productDto) throws FeignException;

    /**
     * Обновляет информацию о существующем товаре в магазине.
     *
     * @param productDto объект {@link ProductDto} с обновленными данными товара
     * @return {@link ProductDto} обновленного товара
     * @throws FeignException в случае ошибки взаимодействия с сервисом
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    ProductDto update(@RequestBody @Valid ProductDto productDto) throws FeignException;

    /**
     * Удаляет товар из магазина по его идентификатору.
     *
     * @param productId идентификатор товара для удаления
     * @return true, если товар успешно удален, false в противном случае
     * @throws FeignException в случае ошибки взаимодействия с сервисом
     */
    @PostMapping("/removeProductFromStore")
    @ResponseStatus(HttpStatus.OK)
    boolean removeProductFromStore(@RequestBody UUID productId) throws FeignException;

    /**
     * Устанавливает состояние количества товара (например, наличие или отсутствие на складе).
     *
     * @param request объект {@link SetProductQuantityStateRequest} с данными для изменения состояния количества
     * @return true, если состояние успешно установлено, false в противном случае
     * @throws FeignException в случае ошибки взаимодействия с сервисом
     */
    @PostMapping("/quantityState")
    @ResponseStatus(HttpStatus.OK)
    boolean setQuantityState(@RequestBody @Valid SetProductQuantityStateRequest request) throws FeignException;

    /**
     * Получает информацию о товаре по его идентификатору.
     *
     * @param productId идентификатор товара для получения информации
     * @return {@link ProductDto} найденного товара
     * @throws FeignException в случае ошибки взаимодействия с сервисом
     */
    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    ProductDto getProduct(@PathVariable UUID productId) throws FeignException;
}