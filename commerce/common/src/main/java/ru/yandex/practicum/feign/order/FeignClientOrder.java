package ru.yandex.practicum.feign.order;

import feign.FeignException;
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
 * Feign-клиент для взаимодействия с сервисом заказов (order service).
 * Предоставляет полный набор методов для управления жизненным циклом заказов:
 * создание, получение, оплата, доставка, сборка и завершение заказов.
 * <p>
 * Все методы соответствуют REST API эндпоинтам сервиса заказов.
 * Используется аннотация {@link FeignClient} для интеграции через Spring Cloud OpenFeign.
 * </p>
 */
@FeignClient(name = "order", path = "/api/v1/order")
public interface FeignClientOrder {

    /**
     * Получает список всех заказов по имени пользователя.
     *
     * @param username имя пользователя, для которого необходимо получить заказы
     * @return список {@link OrderDto} заказов, принадлежащих пользователю
     */
    @GetMapping
    public List<OrderDto> getOrdersByUserName(String username) throws FeignException;

    /**
     * Создает новый заказ на основе предоставленного запроса.
     *
     * @param username имя пользователя, создающего заказ
     * @param request  объект {@link CreateNewOrderRequest} с данными для создания нового заказа
     * @return {@link OrderDto} созданного заказа
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public OrderDto createNewOrderByRequest(
            @RequestParam String username,
            @RequestBody @Valid CreateNewOrderRequest request) throws FeignException;

    /**
     * Обрабатывает возврат товаров в заказе на основе запроса на возврат.
     *
     * @param request объект {@link ProductReturnRequest} с информацией о возвращаемых товарах
     * @return {@link OrderDto} обновленного заказа после обработки возврата
     */
    @PostMapping("/return")
    public OrderDto returnOrderByRequest(@RequestBody @Valid ProductReturnRequest request) throws FeignException;

    /**
     * Выполняет операцию оплаты для заказа с указанным идентификатором.
     *
     * @param orderId идентификатор заказа, который необходимо оплатить
     * @return {@link OrderDto} заказа после успешной оплаты
     */
    @PostMapping("/payment")
    public OrderDto makePaymentByOrderId(@RequestBody @Valid UUID orderId) throws FeignException;

    /**
     * Обрабатывает неудачную попытку оплаты заказа.
     *
     * @param orderId идентификатор заказа, для которого не удалось выполнить оплату
     * @return {@link OrderDto} заказа после обработки неудачной оплаты
     */
    @PostMapping("/payment/failed")
    public OrderDto failedPaymentByOrderId(@RequestBody @Valid UUID orderId) throws FeignException;

    /**
     * Инициирует процесс доставки для заказа с указанным идентификатором.
     *
     * @param orderId идентификатор заказа, который необходимо доставить
     * @return {@link OrderDto} заказа после инициации доставки
     */
    @PostMapping("/delivery")
    public OrderDto deliverByOrderId(@RequestBody @Valid UUID orderId) throws FeignException;

    /**
     * Обрабатывает неудачную попытку доставки заказа.
     *
     * @param orderId идентификатор заказа, доставка которого не удалась
     * @return {@link OrderDto} заказа после обработки неудачной доставки
     */
    @PostMapping("/delivery/failed")
    public OrderDto failedDeliveryByOrderId(@RequestBody @Valid UUID orderId) throws FeignException;

    /**
     * Отмечает заказ как завершенный.
     *
     * @param orderId идентификатор заказа, который необходимо отметить как завершенный
     * @return {@link OrderDto} завершенного заказа
     */
    @PostMapping("/completed")
    public OrderDto completedByOrderId(@RequestBody @Valid UUID orderId) throws FeignException;

    /**
     * Рассчитывает общую стоимость заказа с учетом всех компонентов.
     *
     * @param orderId идентификатор заказа, для которого необходимо рассчитать стоимость
     * @return {@link OrderDto} заказа с рассчитанной общей стоимостью
     */
    @PostMapping("/calculate/total")
    public OrderDto calculateTotalPriceByOrderId(@RequestBody @Valid UUID orderId) throws FeignException;

    /**
     * Инициирует процесс сборки заказа.
     *
     * @param orderId идентификатор заказа, который необходимо собрать
     * @return {@link OrderDto} заказа после инициации сборки
     */
    @PostMapping("/assembly")
    public OrderDto assembleByOrderId(@RequestBody @Valid UUID orderId) throws FeignException;

    /**
     * Обрабатывает неудачную попытку сборки заказа.
     *
     * @param orderId идентификатор заказа, сборка которого не удалась
     * @return {@link OrderDto} заказа после обработки неудачной сборки
     */
    @PostMapping("/assembly/failed")
    public OrderDto failedAssemblyByOrderId(@RequestBody @Valid UUID orderId) throws FeignException;
}