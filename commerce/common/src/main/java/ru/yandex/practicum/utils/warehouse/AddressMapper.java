package ru.yandex.practicum.utils.warehouse;

import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.model.warehouse.Address;

public class AddressMapper {

    public static AddressDto toAddressDto(Address address) {
        if (address == null)
            return null;

        return AddressDto.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .street(address.getCity())
                .house(address.getHouse())
                .flat(address.getFlat())
                .build();
    }

    public static Address toAddress(AddressDto addressDto) {
        if (addressDto == null)
            return null;

        return Address.builder()
                .country(addressDto.getCountry())
                .city(addressDto.getCity())
                .street(addressDto.getCity())
                .house(addressDto.getHouse())
                .flat(addressDto.getFlat())
                .build();
    }
}
