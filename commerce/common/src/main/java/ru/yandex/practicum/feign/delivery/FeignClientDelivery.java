package ru.yandex.practicum.feign.delivery;

import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.delivery.DeliveryDto;

import java.util.UUID;

/**
 * Feign-клиент для взаимодействия с сервисом доставки (delivery service).
 * Предоставляет методы для управления жизненным циклом доставок: создание,
 * отслеживание статусов и получение информации о стоимости доставки.
 * <p>
 * Все методы соответствуют REST API эндпоинтам сервиса доставки.
 * Используется аннотация {@link FeignClient} для интеграции через Spring Cloud OpenFeign.
 * </p>
 */
@FeignClient(name = "delivery",
        contextId = "deliveryApiClient",
        path = "/api/v1/delivery")
public interface FeignClientDelivery {

    /**
     * Создает новую доставку на основе предоставленного объекта DeliveryDto.
     *
     * @param deliveryDto объект {@link DeliveryDto} с данными для создания доставки
     * @return {@link DeliveryDto} созданной доставки
     */
    @PutMapping
    public DeliveryDto createDeliveryByDto(@RequestBody @Valid DeliveryDto deliveryDto) throws FeignException;

    /**
     * Отмечает доставку как успешно завершенную по её идентификатору.
     *
     * @param deliveryId идентификатор доставки, которую необходимо отметить как успешную
     */
    @PostMapping("/successful")
    public void successfulDeliveryById(@RequestBody @Valid UUID deliveryId) throws FeignException;

    /**
     * Отмечает доставку как полученную (забранную) клиентом по её идентификатору.
     *
     * @param deliveryId идентификатор доставки, которую необходимо отметить как полученную
     */
    @PostMapping("/picked")
    public void pickedDeliveryById(@RequestBody @Valid UUID deliveryId) throws FeignException;

    /**
     * Отмечает доставку как неудачную по её идентификатору.
     *
     * @param deliveryId идентификатор доставки, которую необходимо отметить как неудачную
     */
    @PostMapping("/failed")
    public void failedDelivery(@RequestBody @Valid UUID deliveryId) throws FeignException;

    /**
     * Получает стоимость доставки по её идентификатору.
     *
     * @param deliveryId идентификатор доставки, для которой необходимо получить стоимость
     * @return стоимость доставки
     */
    @PostMapping("/cost")
    public Double getDeliveryCostByDeliveryId(@RequestBody @Valid UUID deliveryId) throws FeignException;
}