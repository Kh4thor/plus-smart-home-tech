package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.service.OrderService;
import ru.yandex.practicum.utils.OrderMapper;

import java.util.List;

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

    @PutMapping
    public OrderDto createNewOrderByRequest(
            @RequestParam String username,
            @RequestBody @Valid CreateNewOrderRequest request) {
        orderService.createNewOrderByRequest(username, request);

    }
}
