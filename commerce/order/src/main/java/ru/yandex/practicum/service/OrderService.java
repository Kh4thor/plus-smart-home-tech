package ru.yandex.practicum.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.order.CreateNewOrderRequest;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.order.ProductReturnRequest;
import ru.yandex.practicum.dto.payment.PaymentDto;
import ru.yandex.practicum.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.enums.order.OrderState;
import ru.yandex.practicum.exception.order.NoOrderFoundException;
import ru.yandex.practicum.exception.shopping.cart.ShoppingCartNotFoundException;
import ru.yandex.practicum.feign.payment.FeignClientPayment;
import ru.yandex.practicum.feign.warehouse.FeignClientWarehouse;
import ru.yandex.practicum.model.order.Order;
import ru.yandex.practicum.model.shopping.cart.ShoppingCart;
import ru.yandex.practicum.model.warehouse.Address;
import ru.yandex.practicum.repository.OrderRepository;
import ru.yandex.practicum.utils.order.OrderMapper;
import ru.yandex.practicum.utils.shopping.cart.ShoppingCartMapper;
import ru.yandex.practicum.utils.warehouse.AddressMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
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
        shoppingCartDto = ShoppingCartMapper.toShoppingCartDto(shoppingCart);

        BookedProductsDto bookedProductsDto = feignClientWarehouse.checkProductQuantity(shoppingCartDto);
        boolean fragile = bookedProductsDto.isFragile();
        double deliveryWeight = bookedProductsDto.getDeliveryWeight();
        double deliveryVolume = bookedProductsDto.getDeliveryVolume();
        OrderState newOrderState = OrderState.NEW;

        Order order = Order.builder()
                .shoppingCartId(shoppingCartId)
                .products(products)
                .state(newOrderState)
                .deliveryWeight(deliveryWeight)
                .deliveryVolume(deliveryVolume)
                .fragile(fragile).username(username)
                .address(address)
                .build();

        return orderRepository.save(order);
    }

    @Transactional
    public Order returnOrderByRequest(ProductReturnRequest request) {
        String userMessage = "Unable to return order";
        Order order = getOrderById(request.getOrderId(), userMessage);

        List<OrderState> invalidatedOrderStates = List.of(
                OrderState.PRODUCT_RETURNED,
                OrderState.CANCELED
        );
        if (invalidatedOrderStates.contains(order.getState())) {
            throw new ValidationException(userMessage + ", unexpected order state: " + order.getState());
        }
        feignClientWarehouse.returnProductsToWarehouse(request.getProducts());
        order.setState(OrderState.PRODUCT_RETURNED);
        return order;
    }

    @Transactional
    public Order makePaymentByOrderId(UUID orderId) {
        OrderState newState = OrderState.PAID;
        String userMessage = getUserMessage(newState);
        Order order = getOrderById(orderId, userMessage);
        OrderDto orderDto = OrderMapper.toOrderDto(order);
        PaymentDto paymentDto = feignClientPayment.createPayment(orderDto);
        order.setPaymentId(paymentDto.getPaymentId());
        OrderState expectedCurrentState = OrderState.ON_PAYMENT;
        Order updatedOrder = validateAndSetNewState(newState, expectedCurrentState, order);

        order.setState(OrderState.PAID);
        return orderRepository.save(order);
    }

    @Transactional
    public Order failedPaymentByOrderId(UUID orderId) {
        OrderState newState = OrderState.PAYMENT_FAILED;
        String userMessage = getUserMessage(newState);
        Order order = getOrderById(orderId, userMessage);
        OrderState expectedCurrentState = OrderState.ON_PAYMENT;
        return validateAndSetNewState(newState, expectedCurrentState, order);
    }

    @Transactional
    public Order deliverByOrderId(UUID orderId) {
        OrderState newState = OrderState.ON_DELIVERY;
        String userMessage = getUserMessage(newState);
        Order order = getOrderById(orderId, userMessage);
        OrderState expectedCurrentState = OrderState.ASSEMBLED;
        return validateAndSetNewState(newState, expectedCurrentState, order);
    }

    @Transactional
    public Order failedDeliveryByOrderId(UUID orderId) {
        OrderState newState = OrderState.DELIVERY_FAILED;
        String userMessage = getUserMessage(newState);
        Order order = getOrderById(orderId, userMessage);
        OrderState expectedCurrentState = OrderState.ON_DELIVERY;
        return validateAndSetNewState(newState, expectedCurrentState, order);
    }

    @Transactional
    public Order completedByOrderId(UUID orderId) {
        OrderState newState = OrderState.COMPLETED;
        String userMessage = getUserMessage(newState);
        Order order = getOrderById(orderId, userMessage);
        OrderState expectedCurrentState = OrderState.DELIVERED;
        return validateAndSetNewState(newState, expectedCurrentState, order);
    }

    @Transactional
    public Order calculateTotalPriceByOrderId(UUID orderId) {
        String userMessage = "Unable to calculate total price by order id";
        Order order = getOrderById(orderId, userMessage);
        OrderDto orderDto = OrderMapper.toOrderDto(order);
        Double productsPrice = feignClientPayment.getProductsCostByOrder(orderDto);
        order.setProductPrice(productsPrice);
        Double totalPrice = feignClientPayment.getTotalCost(orderDto);
        order.setTotalPrice(totalPrice);
        return order;
    }

    public Order assembleByOrderId(UUID orderId) {
        OrderState newState = OrderState.ASSEMBLED;
        String userMessage = getUserMessage(newState);
        Order order = getOrderById(orderId, userMessage);
        OrderState expectedCurrentState = OrderState.PAID;
        return validateAndSetNewState(newState, expectedCurrentState, order);
    }

    public Order failedAssemblyByOrderId(UUID orderId) {
        OrderState newState = OrderState.ASSEMBLY_FAILED;
        String userMessage = getUserMessage(newState);
        Order order = getOrderById(orderId, userMessage);
        OrderState expectedCurrentState = OrderState.PAID;
        return validateAndSetNewState(newState, expectedCurrentState, order);
    }

    private Order getOrderById(UUID orderId, String userMessage) {
        return orderRepository.findByOrderId(orderId).orElseThrow(() ->
                new NoOrderFoundException(userMessage, orderId)
        );
    }

    private Order validateAndSetNewState(OrderState newState, OrderState expectedStateOfOrder, Order order) {
        if (order.getState() != expectedStateOfOrder) {
            throw new ValidationException(
                    "Unable to change order status to: " + newState +
                            ". Expected state: " + expectedStateOfOrder +
                            ". Current state: " + order.getState());
        }
        order.setState(newState);
        return orderRepository.save(order);
    }

    private String getUserMessage(OrderState newState) {
        return "Unable to change order status to: " + newState;
    }
}
