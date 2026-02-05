package ru.yandex.practicum.feign.warehouse;

import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Feign-клиент для взаимодействия с сервисом склада (warehouse service).
 * Предоставляет методы для управления товарами на складе: регистрация новых товаров,
 * проверка наличия, добавление товаров и получение информации о складе.
 * <p>
 * Все методы соответствуют REST API эндпоинтам сервиса склада.
 * Используется аннотация {@link FeignClient} для интеграции через Spring Cloud OpenFeign.
 * </p>
 */
@FeignClient(name = "warehouse",
        contextId = "warehouseApiClient",
        path = "/api/v1/warehouse")
public interface FeignClientWarehouse {

    /**
     * Регистрирует новый товар на складе.
     *
     * @param request объект {@link NewProductInWarehouseRequest} с данными нового товара
     * @throws FeignException в случае ошибки взаимодействия с сервисом
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    void registerNewProduct(@RequestBody @Valid NewProductInWarehouseRequest request) throws FeignException;

    /**
     * Проверяет количество и наличие товаров на складе для указанной корзины покупок.
     *
     * @param shoppingCartDto объект {@link ShoppingCartDto} с данными корзины покупок для проверки
     * @return {@link BookedProductsDto} с информацией о забронированных товарах
     * @throws FeignException в случае ошибки взаимодействия с сервисом
     */
    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    BookedProductsDto checkProductQuantity(@RequestBody @Valid ShoppingCartDto shoppingCartDto) throws FeignException;

    /**
     * Добавляет товар на склад.
     *
     * @param request объект {@link AddProductToWarehouseRequest} с данными для добавления товара
     * @throws FeignException в случае ошибки взаимодействия с сервисом
     */
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    void addProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest request) throws FeignException;

    /**
     * Получает адрес склада.
     *
     * @return {@link AddressDto} с адресной информацией склада
     */
    @GetMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    AddressDto getAddress();

    /**
     * Возвращает товары на склад.
     *
     * @param products Карта товаров для возврата на склад, где ключ - UUID товара,
     *                 а значение - количество возвращаемых единиц
     */
    @PostMapping("/return")
    @ResponseStatus(HttpStatus.OK)
    void returnProductsToWarehouse(Map<UUID, Integer> products);

    /**
     * Получает список всех доступных адресов складов.
     * Возвращает перечень всех адресов, на которых расположены склады в системе.
     *
     * @return список строк с названиями/адресами складов
     */
    @GetMapping("/all_addresses")
    List<String> getAllAddresses();

}