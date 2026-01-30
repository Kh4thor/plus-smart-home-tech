package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.enums.order.OrderState;
import ru.yandex.practicum.exception.shopping.cart.ShoppingCartNotFoundException;
import ru.yandex.practicum.feign.payment.FeignClientPayment;
import ru.yandex.practicum.feign.warehouse.FeignClientWarehouse;
import ru.yandex.practicum.model.warehouse.Address;
import ru.yandex.practicum.model.order.Order;
import ru.yandex.practicum.model.shopping.cart.ShoppingCart;
import ru.yandex.practicum.repository.OrderRepository;
import ru.yandex.practicum.utils.AddressMapper;
import ru.yandex.practicum.utils.ShoppingCartMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final FeignClientWarehouse feignClientWarehouse;
    private final FeignClientPayment feignClientPayment;

    public List<Order> getOrdersByUserName(String username) {
        return orderRepository.findAllByUsername(username);
    }

    @Transactional
    public Order createNewOrderByRequest(String username, CreateNewOrderRequest request) {
        AddressDto addressDto = request.getDeliveryAddress();
        Address address = AddressMapper.toAddress(addressDto);
        UUID shoppingCartId = null;
        Map<UUID, Integer> products = new HashMap<>();
        if (request.getShoppingCart() != null) {
            shoppingCartId = request.getShoppingCart().getShoppingCartId();
            products = request.getShoppingCart().getProducts();
        } else {
            String userMessage = "Shopping cart is null";
            throw new ShoppingCartNotFoundException(userMessage, username);
        }

        ShoppingCartDto shoppingCartDto = request.getShoppingCart();
        ShoppingCart shoppingCart = ShoppingCartMapper.toShoppingCart(shoppingCartDto, username);

        BookedProductsDto bookedProductsDto = feignClientWarehouse.checkProductQuantity(shoppingCartDto);
        boolean fragile = bookedProductsDto.isFragile();
        double deliveryWeight = bookedProductsDto.getDeliveryWeight();
        double deliveryVolume = bookedProductsDto.getDeliveryVolume();
        OrderState orderState = OrderState.NEW;

        Order order = Order.builder()
                .shoppingCartId(shoppingCartId)
                .products(products)
                .state(orderState)
                .deliveryWeight(deliveryWeight)
                .deliveryVolume(deliveryVolume)
                .fragile(fragile)
                .username(username)
                .address(address)
                .build();

        return orderRepository.save(order);
    }
}
