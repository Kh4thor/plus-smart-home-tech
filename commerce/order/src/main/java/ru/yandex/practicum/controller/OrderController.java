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

    @GetMapping
    public List<OrderDto> getOrdersByUserName(String username) {
        log.debug("GET /api/v1/order - username: {}", username);
        List<Order> orders = orderService.getOrdersByUserName(username);
        return orders.stream()
                .map(OrderMapper::toOrderDto)
                .toList();
    }

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

    @PostMapping("/return")
    public OrderDto returnOrderByRequest(@RequestBody @Valid ProductReturnRequest request) {
        log.debug("POST /api/v1/order/return - request: {}", request);
        Order returnedOrder = orderService.returnOrderByRequest(request);
        log.info("Returned order: {}", returnedOrder);
        OrderDto returnedOrderDto = OrderMapper.toOrderDto(returnedOrder);
        log.info("Returned order mapped to dto: {}", returnedOrderDto);
        return returnedOrderDto;
    }

    //=== PAYMENT ===
    //TODO
    @PostMapping("/payment")
    public OrderDto makePaymentByOrderId(@RequestBody @Valid UUID orderId) {
        log.debug("POST /api/v1/order/payment - orderId: {}", orderId);
        Order paidOrder = orderService.makePaymentByOrderId(orderId);
        log.info("Paid order: {}", paidOrder);
        OrderDto paidOrderDto = OrderMapper.toOrderDto(paidOrder);
        log.info("Paid order mapped to dto: {}", paidOrderDto);
        return paidOrderDto;
    }

    //TODO
    @PostMapping("/payment/failed")
    public OrderDto failedPaymentByOrderId(@RequestBody @Valid UUID orderId) {
        log.debug("POST /api/v1/order/failed - orderId: {}", orderId);
        Order failedToPayOrder = orderService.failedPaymentByOrderId(orderId);
        log.info("Failed to pay order: {}", failedToPayOrder);
        OrderDto failedToPayOrderDto = OrderMapper.toOrderDto(failedToPayOrder);
        log.info("Failed to pay order mapped to dto: {}", failedToPayOrderDto);
        return failedToPayOrderDto;
    }

    //=== DELIVERY ===
    //TODO
    @PostMapping("/delivery")
    public OrderDto deliverByOrderId(@RequestBody @Valid UUID orderId) {
        log.debug("POST /api/v1/order/delivery - orderId: {}", orderId);
        Order deliveryOrder = orderService.deliverByOrderId(orderId);
        log.info("Delivery order: {}", deliveryOrder);
        OrderDto deliveryOrderDto = OrderMapper.toOrderDto(deliveryOrder);
        log.info("Delivery order mapped to dto: {}", deliveryOrderDto);
        return deliveryOrderDto;
    }

    @PostMapping("/delivery/failed")
    public OrderDto failedDeliveryByOrderId(@RequestBody @Valid UUID orderId) {
        log.debug("POST /api/v1/order/delivery/failed - orderId: {}", orderId);
        Order failedToDeliverOrder = orderService.failedDeliveryByOrderId(orderId);
        log.info("Failed to deliver order: {}", failedToDeliverOrder);
        OrderDto failedToDeliverOrderDto = OrderMapper.toOrderDto(failedToDeliverOrder);
        log.info("Failed to deliver order mapped to dto: {}", failedToDeliverOrderDto);
        return failedToDeliverOrderDto;
    }

    //=== COMPLETED ===
    //TODO
    @PostMapping("/completed")
    public OrderDto completedByOrderId(@RequestBody @Valid UUID orderId) {
        log.debug("POST /api/v1/order/completed - orderId: {}", orderId);
        Order completedOrder = orderService.completedByOrderId(orderId);
        log.info("Completed order: {}", completedOrder);
        OrderDto completedOrderDto = OrderMapper.toOrderDto(completedOrder);
        log.info("Completed order mapped to dto: {}", completedOrderDto);
        return completedOrderDto;
    }

    //=== CALCULATE ===
    @PostMapping("/calculate/total")
    public OrderDto calculateTotalPriceByOrderId(@RequestBody @Valid UUID orderId) {
        log.debug("POST /api/v1/order/calculate/total - orderId: {}", orderId);
        Order calculatedTotalPriceOrder = orderService.calculateTotalPriceByOrderId(orderId);
        log.info("Calculated total price order: {}", calculatedTotalPriceOrder);
        OrderDto calculatedTotalPriceDto = OrderMapper.toOrderDto(calculatedTotalPriceOrder);
        log.info("Calculated total price mapped to dto: {}", calculatedTotalPriceDto);
        return calculatedTotalPriceDto;
    }

    //=== ASSEMBLY ===
    @PostMapping("/assembly")
    public OrderDto assembleByOrderId(@RequestBody @Valid UUID orderId) {
        log.debug("POST /api/v1/order/assembly - orderId: {}", orderId);
        Order assembledOrder = orderService.assembleByOrderId(orderId);
        log.info("Assembled order: {}", assembledOrder);
        OrderDto assembledOrderDto = OrderMapper.toOrderDto(assembledOrder);
        log.info("Assembled order mapped to dto: {}", assembledOrderDto);
        return assembledOrderDto;
    }

    @PostMapping("/assembly/failed")
    public OrderDto failedAssemblyByOrderId(@RequestBody @Valid UUID orderId) {
        log.debug("POST /api/v1/order/assembly/failed - orderId: {}", orderId);
        Order failedToAssembleOrder = orderService.failedAssemblyByOrderId(orderId);
        log.info("Failed to assemble order: {}", failedToAssembleOrder);
        OrderDto failedToAssembleOrderDto = OrderMapper.toOrderDto(failedToAssembleOrder);
        log.info("Failed to assemble order mapped to dto: {}", failedToAssembleOrderDto);
        return failedToAssembleOrderDto;
    }
}
