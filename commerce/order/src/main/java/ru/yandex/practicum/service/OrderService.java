package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.exception.shopping.cart.ShoppingCartNotFoundException;
import ru.yandex.practicum.feign.shopping.store.FeignClientShoppingStore;
import ru.yandex.practicum.feign.warehouse.FeignClientWarehouse;
import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.model.ShoppingCart;
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
    private final FeignClientShoppingStore feignClientShoppingStore;

    public List<Order> getOrdersByUserName(String username) {
        return orderRepository.findAllByUsername(username);
    }

    public void createNewOrderByRequest(String username, CreateNewOrderRequest request) {
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

        feignClientShoppingStore.


        Product product = new Product();


        Order order = Order.builder()
                .orderId()
                .shoppingCartId(shoppingCartId)
                .products(products)
                .paymentId(
                .deliveryId(
                .state(
                .deliveryWeight(deliveryWeight)
                .deliveryVolume(deliveryVolume)
                .fragile(fragile)
                .totalPrice(
                .deliveryPrice(
                .productPrice(
                .username(username)
                .address(address)
                .build();

        Order newOrder = Order.builder()
                .deliveryWeight(bookedProductsDto.getDeliveryWeight())
                .deliveryVolume(bookedProductsDto.getDeliveryVolume())
                .build();

        return toDto(orderRepository.save(newOrder));
    }

    private double getTotalPrice(Order order) {}

}
