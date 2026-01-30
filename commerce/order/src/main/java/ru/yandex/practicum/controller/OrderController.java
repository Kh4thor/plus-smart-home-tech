package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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

@RestController
@RequestMapping("/api/v1/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> getOrdersByUserName(String userName) {
        List<Order> orders = orderService.getOrdersByUserName(userName);
        return orders.stream()
                .map(OrderMapper::toOrderDto)
                .toList();
    }

    //TODO
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public OrderDto createNewOrderByRequest(
            @RequestParam String username,
            @RequestBody @Valid CreateNewOrderRequest request) {
        Order newOrder = orderService.createNewOrderByRequest(username, request);
        return OrderMapper.toOrderDto(newOrder);
    }

    //TODO
    @PostMapping("/return")
    public ProductReturnRequest returnOrder(@RequestBody @Valid ProductReturnRequest productReturnRequest) {
        return productReturnRequest;
    }

    //=== PAYMENT ===
    //TODO
    @PostMapping("/payment")
    public OrderDto paymentByOrder(@RequestBody @Valid UUID orderId) {
        return new OrderDto();
    }

    //TODO
    @PostMapping("/payment/failed")
    public OrderDto failedPaymentByOrder(@RequestBody @Valid UUID orderId) {
        return new OrderDto();
    }

    //=== DELIVERY ===
    //TODO
    @PostMapping("/delivery")
    public OrderDto deliveryByOrder(@RequestBody @Valid UUID orderId) {
        return new OrderDto();
    }

    //TODO
    @PostMapping("/delivery/failed")
    public OrderDto failedDeliveryByOrder(@RequestBody @Valid UUID orderId) {
        return new OrderDto();
    }

    //=== COMPLETED ===
    //TODO
    @PostMapping("/completed")
    public OrderDto completedOrder(@RequestBody @Valid UUID orderId) {
        return new OrderDto();
    }

    //=== CALCULATE ===
    @PostMapping("/calculate/total")
    public OrderDto calculateTotalPriceByOrder(@RequestBody @Valid UUID orderId) {
        return new OrderDto();
    }

    //=== ASSEMBLY ===
    @PostMapping("/assembly")
    public OrderDto assemblyByOrder(@RequestBody @Valid UUID orderId) {
        return new OrderDto();
    }

    @PostMapping("/assembly/failed")
    public OrderDto failedAssemblyByOrder(@RequestBody @Valid UUID orderId) {
        return new OrderDto();
    }
}
