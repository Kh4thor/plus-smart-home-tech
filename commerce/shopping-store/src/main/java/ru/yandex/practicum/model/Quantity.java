package ru.yandex.practicum.model;

public enum Quantity {
    ENDED, // товар закончился
    FEW, // осталось меньше 10 ед.
    ENOUGH, // осталось от 10 до 100 ед.
    MANY; // больше 100 ед.
}
