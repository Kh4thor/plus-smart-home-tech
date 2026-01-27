package ru.yandex.practicum.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public List<OrderDto> getOrdersByUserName(String userName) {
       List <Order> orders = orderService.getOrdersByUserName(userName);
    }
}
