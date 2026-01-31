package ru.yandex.practicum.feign.payment;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.order.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

/**
 * Feign-клиент для взаимодействия с сервисом платежей (payment service).
 * Предоставляет методы для работы с заказами, платежами, доставкой, сборкой и расчетами.
 * <p>
 * Все методы соответствуют REST API эндпоинтам сервиса платежей.
 * Используется аннотация {@link FeignClient} для интеграции через Spring Cloud OpenFeign.
 * </p>
 */
@FeignClient(name = "payment", path = "/api/v1/payment")
public interface FeignClientPayment {

    /**
     * Получает список заказов по имени пользователя.
     *
     * @param username имя пользователя для поиска заказов
     * @return список {@link OrderDto} заказов пользователя
     */
    @GetMapping
    public List<OrderDto> getOrdersByUserName(String username);

    /**
     * Создает новый заказ на основе запроса.
     *
     * @param username имя пользователя, создающего заказ
     * @param request  {@link CreateNewOrderRequest} с данными для создания заказа
     * @return {@link OrderDto} созданного заказа
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public OrderDto createNewOrderByRequest(
            @RequestParam String username,
            @RequestBody @Valid CreateNewOrderRequest request);

    /**
     * Обрабатывает возврат товаров на основе запроса.
     *
     * @param request {@link ProductReturnRequest} с данными для возврата товаров
     * @return {@link OrderDto} обновленного заказа после возврата
     */
    @PostMapping("/return")
    public OrderDto returnOrderByRequest(@RequestBody @Valid ProductReturnRequest request);

    /**
     * Выполняет платеж по идентификатору заказа.
     *
     * @param orderId идентификатор заказа для оплаты
     * @return {@link OrderDto} заказа после проведения платежа
     */
    @PostMapping("/payment")
    public OrderDto makePaymentByOrderId(@RequestBody @Valid UUID orderId);

    /**
     * Обрабатывает неудачный платеж по идентификатору заказа.
     *
     * @param orderId идентификатор заказа с неудачным платежом
     * @return {@link OrderDto} заказа после обработки неудачного платежа
     */
    @PostMapping("/payment/failed")
    public OrderDto failedPaymentByOrderId(@RequestBody @Valid UUID orderId);

    /**
     * Инициирует доставку заказа по идентификатору.
     *
     * @param orderId идентификатор заказа для доставки
     * @return {@link OrderDto} заказа после инициации доставки
     */
    @PostMapping("/delivery")
    public OrderDto deliverByOrderId(@RequestBody @Valid UUID orderId);

    /**
     * Обрабатывает неудачную доставку заказа по идентификатору.
     *
     * @param orderId идентификатор заказа с неудачной доставкой
     * @return {@link OrderDto} заказа после обработки неудачной доставки
     */
    @PostMapping("/delivery/failed")
    public OrderDto failedDeliveryByOrderId(@RequestBody @Valid UUID orderId);

    /**
     * Отмечает заказ как завершенный по идентификатору.
     * <p>
     * TODO: Требуется реализация.
     * </p>
     *
     * @param orderId идентификатор заказа для завершения
     * @return {@link OrderDto} завершенного заказа
     */
    @PostMapping("/completed")
    public OrderDto completedByOrderId(@RequestBody @Valid UUID orderId);

    /**
     * Рассчитывает общую стоимость заказа по идентификатору.
     *
     * @param orderId идентификатор заказа для расчета стоимости
     * @return {@link OrderDto} заказа с рассчитанной общей стоимостью
     */
    @PostMapping("/calculate/total")
    public OrderDto calculateTotalPriceByOrderId(@RequestBody @Valid UUID orderId);

    /**
     * Инициирует сборку заказа по идентификатору.
     *
     * @param orderId идентификатор заказа для сборки
     * @return {@link OrderDto} заказа после инициации сборки
     */
    @PostMapping("/assembly")
    public OrderDto assembleByOrderId(@RequestBody @Valid UUID orderId);

    /**
     * Обрабатывает неудачную сборку заказа по идентификатору.
     *
     * @param orderId идентификатор заказа с неудачной сборкой
     * @return {@link OrderDto} заказа после обработки неудачной сборки
     */
    @PostMapping("/assembly/failed")
    public OrderDto failedAssemblyByOrderId(@RequestBody @Valid UUID orderId);
}