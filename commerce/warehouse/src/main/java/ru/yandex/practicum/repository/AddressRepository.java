package ru.yandex.practicum.repository;

import org.springframework.stereotype.Repository;

import java.security.SecureRandom;
import java.util.Random;

@Repository
public class AddressRepository {
    private static final String[] ADDRESSES =
            new String[]{"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];

    public String getAddress() {
        return CURRENT_ADDRESS;
    }
}
