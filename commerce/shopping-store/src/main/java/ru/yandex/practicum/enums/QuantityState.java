package ru.yandex.practicum.enums;

public enum QuantityState {
    ENDED, // товар закончился
    FEW, // осталось меньше 10 ед.
    ENOUGH, // осталось от 10 до 100 ед.
    MANY; // больше 100 ед.
}
