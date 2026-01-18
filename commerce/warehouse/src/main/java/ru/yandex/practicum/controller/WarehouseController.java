package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.yandex.practicum.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.exception.warehouse.WarehouseProductNotFoundException;
import ru.yandex.practicum.service.WarehouseService;

/**
 * REST контроллер для управления складскими операциями.
 * Предоставляет API для регистрации товаров на складе, проверки их наличия,
 * установки количества и получения адреса склада.
 *
 * <p>Все эндпоинты доступны по базовому пути: {@code /api/v1/warehouse}</p>
 *
 * <p>Контроллер выполняет валидацию входных данных с использованием аннотаций Bean Validation.
 * Все методы возвращают HTTP статус 200 OK при успешном выполнении.</p>
 *
 * @see WarehouseService
 * @see NewProductInWarehouseRequest
 * @see ShoppingCartDto
 * @see AddProductToWarehouseRequest
 * @see AddressDto
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {

    /**
     * Сервис для выполнения бизнес-логики складских операций.
     * Инжектируется через конструктор с помощью Lombok {@link RequiredArgsConstructor}.
     */
    private final WarehouseService warehouseService;

    /**
     * Регистрирует новый товар на складе.
     * Создает карточку товара с характеристиками (вес, габариты, хрупкость) и сохраняет ее в системе.
     *
     * @param request DTO с данными нового товара для регистрации
     * @throws jakarta.validation.ConstraintViolationException если данные товара не проходят валидацию
     * @throws SpecifiedProductAlreadyInWarehouseException если товар с таким ID уже зарегистрирован
     * @apiNote Использует HTTP метод PUT для создания нового ресурса
     * @see NewProductInWarehouseRequest
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void registerNewProduct(@RequestBody @Valid NewProductInWarehouseRequest request) {
        warehouseService.registerNewProduct(request);
    }

    /**
     * Проверяет наличие товаров на складе и рассчитывает характеристики доставки.
     * Выполняет проверку достаточности количества каждого товара и рассчитывает
     * общий вес, объем и определяет, является ли заказ хрупким.
     *
     * @param shoppingCartDto DTO корзины покупок с товарами и их количествами
     * @return DTO с характеристиками доставки: вес, объем и флаг хрупкости
     * @throws jakarta.validation.ConstraintViolationException если данные корзины не проходят валидацию
     * @throws NoSpecifiedProductInWarehouseException если один или несколько товаров не найдены на складе
     * @throws ProductInShoppingCartLowQuantityInWarehouseException если количество товара на складе недостаточно
     * @see ShoppingCartDto
     * @see BookedProductsDto
     */
    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public BookedProductsDto checkProductQuantity(@RequestBody @Valid ShoppingCartDto shoppingCartDto) {
        return warehouseService.checkProductQuantity(shoppingCartDto);
    }

    /**
     * Устанавливает количество для существующего товара на складе.
     * Находит товар по идентификатору и устанавливает указанное количество,
     * заменяя предыдущее значение.
     *
     * @param request DTO запроса с идентификатором товара и новым количеством
     * @throws jakarta.validation.ConstraintViolationException если данные запроса не проходят валидацию
     * @throws WarehouseProductNotFoundException если товар с указанным ID не найден
     * @apiNote Несмотря на название метода "add", он не добавляет к существующему количеству,
     *          а устанавливает новое значение
     * @see AddProductToWarehouseRequest
     */
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void addProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest request) {
        warehouseService.addProduct(request);
    }

    /**
     * Получает адрес склада.
     * Возвращает структурированную информацию об адресе склада.
     *
     * @param addressDto DTO адреса для валидации и структурирования ответа
     * @return DTO с адресом склада
     * @throws jakarta.validation.ConstraintViolationException если DTO адреса не проходит валидацию
     * @apiNote Использует GET метод с телом запроса ({@link RequestBody}), что нестандартно для HTTP GET
     * @see AddressDto
     */
    @GetMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    public AddressDto getAddress() {
        return warehouseService.getAddress();
    }
}