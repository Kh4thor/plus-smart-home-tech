package ru.yandex.practicum.feign.payment;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.payment.PaymentDto;

/**
 * Feign-клиент для взаимодействия с сервисом платежей (payment service).
 * Предоставляет методы для создания платежей, расчета стоимостей,
 * обработки возвратов и неудачных платежей.
 * <p>
 * Все методы соответствуют REST API эндпоинтам сервиса платежей.
 * Используется аннотация {@link FeignClient} для интеграции через Spring Cloud OpenFeign.
 * </p>
 */
@FeignClient(name = "payment",
        contextId = "paymentApiClient",
        path = "/api/v1/payment")
public interface FeignClientPayment {

    /**
     * Создает новый платеж на основе данных заказа.
     *
     * @param orderDto объект {@link OrderDto} с данными заказа для создания платежа
     * @return {@link PaymentDto} созданный платеж
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentDto createPayment(@RequestBody @Valid OrderDto orderDto);

    /**
     * Рассчитывает общую стоимость для указанного заказа.
     *
     * @param orderDto объект {@link OrderDto} с данными заказа для расчета стоимости
     * @return общая стоимость заказа
     */
    @PostMapping("/totalCost")
    @ResponseStatus(HttpStatus.OK)
    public Double getTotalCost(@RequestBody @Valid OrderDto orderDto);

    /**
     * Обрабатывает возврат платежа для указанного заказа.
     * Метод возвращает статус NOT_FOUND (404).
     *
     * @param orderDto объект {@link OrderDto} с данными заказа для возврата платежа
     */
    @PostMapping("/refund")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void refundPayment(@RequestBody @Valid OrderDto orderDto);

    /**
     * Получает стоимость продуктов в указанном заказе.
     *
     * @param orderDto объект {@link OrderDto} с данными заказа для расчета стоимости продуктов
     * @return стоимость продуктов в заказе
     */
    @PostMapping("/productCost")
    @ResponseStatus(HttpStatus.OK)
    public Double getProductsCostByOrder(@RequestBody @Valid OrderDto orderDto);

    /**
     * Обрабатывает неудачный платеж для указанного заказа.
     * Метод возвращает статус NOT_FOUND (404).
     *
     * @param orderDto объект {@link OrderDto} с данными заказа с неудачным платежом
     */
    @PostMapping("/failed")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void failedPayment(@RequestBody @Valid OrderDto orderDto);
}