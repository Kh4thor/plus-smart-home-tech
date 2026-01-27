package ru.yandex.practicum.utils;

import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.model.Order;

public class OrderMapper {

    public static OrderDto toOrderDto(Order order) {
        if (order == null)
            return null;

        return OrderDto.builder()
                .orderId(order.getOrderId())
                .shoppingCartId(order.getShoppingCartId())
                .products(order.getProducts())
                .paymentId(order.getPaymentId())
                .deliveryId(order.getDeliveryId())
                .state(order.getState())
                .deliveryWeight(order.getDeliveryWeight())
                .deliveryVolume(order.getDeliveryVolume())
                .fragile(order.isFragile())
                .totalPrice(order.getTotalPrice())
                .deliveryPrice(order.getDeliveryPrice())
                .build();
    }

    public static Order toOrder(OrderDto orderDto, String username, Double productPrice, Address address) {
        if (orderDto == null)
            return null;

        return Order.builder()
                .orderId(orderDto.getOrderId())
                .shoppingCartId(orderDto.getShoppingCartId())
                .products(orderDto.getProducts())
                .paymentId(orderDto.getPaymentId())
                .deliveryId(orderDto.getDeliveryId())
                .state(orderDto.getState())
                .deliveryWeight(orderDto.getDeliveryWeight())
                .deliveryVolume(orderDto.getDeliveryVolume())
                .fragile(orderDto.isFragile())
                .totalPrice(orderDto.getTotalPrice())
                .deliveryPrice(orderDto.getDeliveryPrice())
                .productPrice(productPrice)
                .username(username)
                .address(address)
                .build();
    }
}
