package ru.yandex.practicum.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.enums.delivery.DeliveryState;
import ru.yandex.practicum.exception.delivery.NoDeliveryFoundException;
import ru.yandex.practicum.model.delivery.Delivery;
import ru.yandex.practicum.model.order.Order;
import ru.yandex.practicum.model.warehouse.Address;
import ru.yandex.practicum.repository.DeliveryRepository;
import ru.yandex.practicum.repository.OrderRepository;
import ru.yandex.practicum.utils.warehouse.AddressMapper;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final WarehouseService warehouseService;

    public Delivery createDeliveryByDto(DeliveryDto deliveryDto) {
        String userMessage = "Unable to create delivery.";
        Address fromAddress = AddressMapper.toAddress(deliveryDto.getFromAddress());
        Address toAddress = AddressMapper.toAddress(deliveryDto.getToAddress());
        Delivery delivery = Delivery.builder().fromAddress(fromAddress).toAddress(toAddress).orderId(deliveryDto.getOrderId()).state(DeliveryState.CREATED).build();
        Delivery saved = deliveryRepository.save(delivery);
        UUID orderId = saved.getOrderId();
        UUID deliveryId = saved.getDeliveryId();
        Order order = orderService.getOrderById(orderId, userMessage);
        order.setDeliveryId(deliveryId);
        orderRepository.save(order);
        return delivery;
    }

    public Delivery successfulDeliveryById(UUID deliveryId) {
        return changeDeliveryState(deliveryId, DeliveryState.IN_PROGRESS, DeliveryState.DELIVERED);
    }

    public Delivery pickedDeliveryById(UUID deliveryId) {
        return changeDeliveryState(deliveryId, DeliveryState.CREATED, DeliveryState.IN_PROGRESS);
    }

    public Delivery failedDelivery(UUID deliveryId) {
        return changeDeliveryState(deliveryId, DeliveryState.IN_PROGRESS, DeliveryState.FAILED);
    }

    public Double getDeliveryCost(OrderDto orderDto) {
        String userMessage = "Unable to get delivery cost";
        UUID deliveryId = orderDto.getDeliveryId();
        Delivery delivery = getDelivery(deliveryId, userMessage);

        Address fromAddress = delivery.getFromAddress();
        Address toAddress = delivery.getToAddress();

        String toAddressName = toAddress.getAddressName();

        double weight = orderDto.getDeliveryWeight();
        double volume = orderDto.getDeliveryVolume();

        final double baseIndex = 5.0;
        final double weightIndex = 0.3;
        final double volumeIndex = 0.2;
        final double remoteIndex = getRemoteIndex(fromAddress, toAddress);
        final double fragileIndex = getFragileIndex(orderDto);
        final double warehouseAddressIndex = getWarehouseIndex(toAddressName);

        double totalCost = baseIndex * warehouseAddressIndex + baseIndex; // с учетом адреса склада
        totalCost += totalCost * fragileIndex; // с учетом хрупкости товара
        totalCost += weight * weightIndex; // с учетом веса товара
        totalCost += volume * volumeIndex; // с учетом объема товара
        totalCost += totalCost * remoteIndex; // с учетом удаленности адреса доставки

        return totalCost;
    }

    private double getFragileIndex(OrderDto orderDto) {
        if (orderDto == null) {
            throw new IllegalArgumentException("OrderDto must be not null");
        }
        final double notFragileIndex = 0.0;
        final double fragileIndex = 0.2;

        if (orderDto.isFragile()) {
            return fragileIndex;
        }
        return notFragileIndex;
    }

    private double getRemoteIndex(Address fromAddress, Address toAddress) {
        if (fromAddress == null || toAddress == null) {
            throw new IllegalArgumentException("Address must be not null");
        }

        final double baseRemoteIndex = 0.0;
        final double sameStreetIndex = 0.2;

        return Objects.equals(fromAddress.getCountry(), toAddress.getCountry())
                && Objects.equals(fromAddress.getCity(), toAddress.getCity())
                && Objects.equals(fromAddress.getStreet(), toAddress.getStreet())
                ? baseRemoteIndex : sameStreetIndex;
    }

    private double getWarehouseIndex(String warehouseName) {
        List<String> warehouseNames = warehouseService.getAllAddresses();
        if (warehouseNames.contains(warehouseName)) {
            String[] split = warehouseName.split("_");
            String index = split[1];
            try {
                return Double.parseDouble(index);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Incorrect index of warehouse name: " + warehouseName, e);
            }
        }
        throw new ValidationException("Unable to get warehouse index");
    }

    private Delivery getDelivery(UUID deliveryId, String userMessage) {
        return deliveryRepository.findByDeliveryId().orElseThrow(
                () -> new NoDeliveryFoundException(userMessage, deliveryId));
    }

    private Delivery changeDeliveryState(UUID deliveryId, DeliveryState expectedState, DeliveryState newState) {

        String userMessage = "Unable to change delivery status to: " + newState;
        Delivery delivery = getDelivery(deliveryId, userMessage);

        if (delivery.getState() != expectedState) {
            throw new ValidationException("Unable to change delivery status to: " + newState + "" +
                    ". Expected state: " + expectedState + ". Current state: " + delivery.getState());
        }
        delivery.setState(newState);
        return deliveryRepository.save(delivery);
    }
}