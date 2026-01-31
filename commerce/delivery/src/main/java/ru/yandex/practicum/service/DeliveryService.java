package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.model.delivery.Delivery;
import ru.yandex.practicum.repository.DeliveryRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public Delivery createDeliveryByDto(DeliveryDto deliveryDto) {
        return null;
    }

    public void successfulDeliveryById(UUID deliveryId) {
    }

    public void pickedDeliveryById(UUID deliveryId) {
    }

    public void failedDelivery(UUID deliveryId) {
    }

    public Double getDeliveryCostByDeliveryId(UUID deliveryId) {
        return null;
    }
}
