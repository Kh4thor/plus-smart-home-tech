package ru.yandex.practicum.utils.delivery;

import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.model.delivery.Delivery;
import ru.yandex.practicum.model.warehouse.Address;
import ru.yandex.practicum.utils.warehouse.AddressMapper;

public class DeliveryMapper {

    public static DeliveryDto toDeliveryDto(Delivery delivery) {
        if (delivery == null)
            return null;

        AddressDto fromAddressDto = AddressMapper.toAddressDto(delivery.getFromAddress());
        AddressDto toAddressDto = AddressMapper.toAddressDto(delivery.getToAddress());

        return DeliveryDto.builder()
                .deliveryId(delivery.getDeliveryId())
                .fromAddress(fromAddressDto)
                .toAddress(toAddressDto)
                .orderId(delivery.getOrderId())
                .state(delivery.getState())
                .build();
    }

    public static Delivery toDelivery(DeliveryDto deliveryDto) {
        if (deliveryDto == null)
            return null;

        Address fromAddress = AddressMapper.toAddress(deliveryDto.getFromAddress());
        Address toAddress = AddressMapper.toAddress(deliveryDto.getToAddress());

        return Delivery.builder()
                .deliveryId(deliveryDto.getDeliveryId())
                .fromAddress(fromAddress)
                .toAddress(toAddress)
                .orderId(deliveryDto.getOrderId())
                .state(deliveryDto.getState())
                .build();
    }
}