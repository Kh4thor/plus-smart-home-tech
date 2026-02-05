package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.order.ProductReturnRequest;
import ru.yandex.practicum.model.order.Order;
import ru.yandex.practicum.service.OrderService;
import ru.yandex.practicum.utils.order.OrderMapper;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Получает список всех заказов для указанного пользователя.
     * Возвращает перечень заказов, связанных с заданным именем пользователя.
     *
     * @param username имя пользователя, для которого запрашиваются заказы
     * @return список {@link OrderDto} объектов, представляющих заказы пользователя
     */
    @GetMapping
    public List<OrderDto> getOrdersByUserName(String username) {
        log.debug("GET /api/v1/order - username: {}", username);
        List<Order> orders = orderService.getOrdersByUserName(username);
        return orders.stream()
                .map(OrderMapper::toOrderDto)
                .toList();
    }

    /**
     * Создает новый заказ на основе полученного запроса.
     * Принимает данные для создания заказа и имя пользователя,
     * создает заказ и возвращает его DTO представление.
     *
     * @param username имя пользователя, создающего заказ
     * @param request объект {@link CreateNewOrderRequest} с данными для создания заказа
     * @return {@link OrderDto} созданный заказ
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public OrderDto createNewOrderByRequest(
            @RequestParam String username,
            @RequestBody @Valid CreateNewOrderRequest request) {
        log.debug("PUT /api/v1/order - username: {}, request: {}", username, request);
        Order createdOrder = orderService.createNewOrderByRequest(username, request);
        log.info("Created order: {}", createdOrder);
        OrderDto createdOrderDto = OrderMapper.toOrderDto(createdOrder);
        log.info("Created order mapped to dto: {}", createdOrderDto);
        return createdOrderDto;
    }

    /**
     * Обрабатывает возврат товаров по указанному заказу.
     * Принимает запрос на возврат продуктов, обновляет статус заказа
     * и возвращает обновленное DTO заказа.
     *
     * @param request объект {@link ProductReturnRequest} с данными для возврата товаров
     * @return {@link OrderDto} заказ с обновленным статусом возврата
     */
    @PostMapping("/return")
    public OrderDto returnOrderByRequest(@RequestBody @Valid ProductReturnRequest request) {
        log.debug("POST /api/v1/order/return - request: {}", request);
        Order returnedOrder = orderService.returnOrderByRequest(request);
        log.info("Returned order: {}", returnedOrder);
        OrderDto returnedOrderDto = OrderMapper.toOrderDto(returnedOrder);
        log.info("Returned order mapped to dto: {}", returnedOrderDto);
        return returnedOrderDto;
    }

    /**
     * Обрабатывает успешную оплату заказа по его идентификатору.
     * Обновляет статус заказа на "оплачен" и возвращает обновленное DTO.
     *
     * @param orderId идентификатор заказа для обработки оплаты
     * @return {@link OrderDto} заказ с обновленным статусом оплаты
     */
    @PostMapping("/payment")
    public OrderDto makePaymentByOrderId(@RequestBody UUID orderId) {
        log.debug("POST /api/v1/order/payment - order id: {}", orderId);
        Order orderUpdated = orderService.makePaymentByOrderId(orderId);
        log.info("Paid order: {}", orderUpdated);
        OrderDto orderUpdatedDto = OrderMapper.toOrderDto(orderUpdated);
        log.info("Paid order mapped to dto: {}", orderUpdatedDto);
        return orderUpdatedDto;
    }

    /**
     * Обрабатывает неудачную попытку оплаты заказа.
     * Обновляет статус заказа на "оплата не удалась" и возвращает обновленное DTO.
     *
     * @param orderId идентификатор заказа с неудачной оплатой
     * @return {@link OrderDto} заказ с обновленным статусом неудачной оплаты
     */
    @PostMapping("/payment/failed")
    public OrderDto failedPaymentByOrderId(@RequestBody @Valid UUID orderId) {
        log.debug("POST /api/v1/order/failed - orderId: {}", orderId);
        Order failedToPayOrder = orderService.failedPaymentByOrderId(orderId);
        log.info("Failed to pay order: {}", failedToPayOrder);
        OrderDto failedToPayOrderDto = OrderMapper.toOrderDto(failedToPayOrder);
        log.info("Failed to pay order mapped to dto: {}", failedToPayOrderDto);
        return failedToPayOrderDto;
    }

    /**
     * Инициирует процесс доставки для указанного заказа.
     * Обновляет статус заказа на "в доставке" и возвращает обновленное DTO.
     *
     * @param orderId идентификатор заказа для доставки
     * @return {@link OrderDto} заказ с обновленным статусом доставки
     */
    @PostMapping("/delivery")
    public OrderDto deliverByOrderId(@RequestBody @Valid UUID orderId) {
        log.debug("POST /api/v1/order/delivery - orderId: {}", orderId);
        Order deliveryOrder = orderService.deliverByOrderId(orderId);
        log.info("Delivery order: {}", deliveryOrder);
        OrderDto deliveryOrderDto = OrderMapper.toOrderDto(deliveryOrder);
        log.info("Delivery order mapped to dto: {}", deliveryOrderDto);
        return deliveryOrderDto;
    }

    /**
     * Обрабатывает неудачную попытку доставки заказа.
     * Обновляет статус заказа на "доставка не удалась" и возвращает обновленное DTO.
     *
     * @param orderId идентификатор заказа с неудачной доставкой
     * @return {@link OrderDto} заказ с обновленным статусом неудачной доставки
     */
    @PostMapping("/delivery/failed")
    public OrderDto failedDeliveryByOrderId(@RequestBody @Valid UUID orderId) {
        log.debug("POST /api/v1/order/delivery/failed - orderId: {}", orderId);
        Order failedToDeliverOrder = orderService.failedDeliveryByOrderId(orderId);
        log.info("Failed to deliver order: {}", failedToDeliverOrder);
        OrderDto failedToDeliverOrderDto = OrderMapper.toOrderDto(failedToDeliverOrder);
        log.info("Failed to deliver order mapped to dto: {}", failedToDeliverOrderDto);
        return failedToDeliverOrderDto;
    }

    /**
     * Отмечает заказ как успешно завершенный.
     * Обновляет статус заказа на "завершен" и возвращает обновленное DTO.
     *
     * @param orderId идентификатор завершаемого заказа
     * @return {@link OrderDto} заказ с обновленным статусом завершения
     */
    @PostMapping("/completed")
    public OrderDto completedByOrderId(@RequestBody @Valid UUID orderId) {
        log.debug("POST /api/v1/order/completed - orderId: {}", orderId);
        Order completedOrder = orderService.completedByOrderId(orderId);
        log.info("Completed order: {}", completedOrder);
        OrderDto completedOrderDto = OrderMapper.toOrderDto(completedOrder);
        log.info("Completed order mapped to dto: {}", completedOrderDto);
        return completedOrderDto;
    }

    /**
     * Рассчитывает общую стоимость указанного заказа.
     * Выполняет расчет стоимости заказа и возвращает обновленное DTO с рассчитанной стоимостью.
     *
     * @param orderId идентификатор заказа для расчета стоимости
     * @return {@link OrderDto} заказ с рассчитанной общей стоимостью
     */
    @PostMapping("/calculate/total")
    public OrderDto calculateTotalPriceByOrderId(@RequestBody @Valid UUID orderId) {
        log.debug("POST /api/v1/order/calculate/total - orderId: {}", orderId);
        Order calculatedTotalPriceOrder = orderService.calculateTotalPriceByOrderId(orderId);
        log.info("Calculated total price order: {}", calculatedTotalPriceOrder);
        OrderDto calculatedTotalPriceDto = OrderMapper.toOrderDto(calculatedTotalPriceOrder);
        log.info("Calculated total price mapped to dto: {}", calculatedTotalPriceDto);
        return calculatedTotalPriceDto;
    }

    /**
     * Инициирует процесс сборки (комплектации) заказа.
     * Обновляет статус заказа на "собран" и возвращает обновленное DTO.
     *
     * @param orderId идентификатор заказа для сборки
     * @return {@link OrderDto} заказ с обновленным статусом сборки
     */
    @PostMapping("/assembly")
    public OrderDto assembleByOrderId(@RequestBody @Valid UUID orderId) {
        log.debug("POST /api/v1/order/assembly - orderId: {}", orderId);
        Order assembledOrder = orderService.assembleByOrderId(orderId);
        log.info("Assembled order: {}", assembledOrder);
        OrderDto assembledOrderDto = OrderMapper.toOrderDto(assembledOrder);
        log.info("Assembled order mapped to dto: {}", assembledOrderDto);
        return assembledOrderDto;
    }

    /**
     * Обрабатывает неудачную попытку сборки заказа.
     * Обновляет статус заказа на "сборка не удалась" и возвращает обновленное DTO.
     *
     * @param orderId идентификатор заказа с неудачной сборкой
     * @return {@link OrderDto} заказ с обновленным статусом неудачной сборки
     */
    @PostMapping("/assembly/failed")
    public OrderDto failedAssemblyByOrderId(@RequestBody @Valid UUID orderId) {
        log.debug("POST /api/v1/order/assembly/failed - orderId: {}", orderId);
        Order failedToAssembleOrder = orderService.failedAssemblyByOrderId(orderId);
        log.info("Failed to assemble order: {}", failedToAssembleOrder);
        OrderDto failedToAssembleOrderDto = OrderMapper.toOrderDto(failedToAssembleOrder);
        log.info("Failed to assemble order mapped to dto: {}", failedToAssembleOrderDto);
        return failedToAssembleOrderDto;
    }

    /**
     * Получает заказ по его идентификатору с пользовательским сообщением об ошибке.
     * Используется для внутренних вызовов с кастомными сообщениями об ошибках.
     *
     * @param orderId идентификатор запрашиваемого заказа
     * @param userMessage пользовательское сообщение для отображения в случае ошибки
     * @return {@link Order} объект заказа
     */
    @GetMapping
    public Order getOrderById(@RequestParam UUID orderId, String userMessage) {
        return orderService.getOrderById(orderId, userMessage);
    }
}