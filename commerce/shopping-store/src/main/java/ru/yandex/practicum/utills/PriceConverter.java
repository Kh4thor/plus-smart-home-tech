package ru.yandex.practicum.utills;

public class PriceConverter {

    public static Integer toCents(Double price) {
        if (price == null) return null;
        return (int) (price * 100);
    }

    public static Double toRoubles(Integer price) {
        if (price == null) return null;
        return (double) (price/100.0);
    }
}
