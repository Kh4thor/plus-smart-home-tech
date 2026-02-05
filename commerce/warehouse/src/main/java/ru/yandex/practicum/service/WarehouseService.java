package ru.yandex.practicum.service;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.controller.ShippedToDeliveryRequest;
import ru.yandex.practicum.dto.delivery.DeliveryDto;
import ru.yandex.practicum.dto.order.OrderDto;
import ru.yandex.practicum.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.*;
import ru.yandex.practicum.enums.delivery.DeliveryState;
import ru.yandex.practicum.enums.order.OrderState;
import ru.yandex.practicum.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.yandex.practicum.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.exception.warehouse.WarehouseProductNotFoundException;
import ru.yandex.practicum.feign.delivery.FeignClientDelivery;
import ru.yandex.practicum.feign.order.FeignClientOrder;
import ru.yandex.practicum.model.order.Order;
import ru.yandex.practicum.model.warehouse.Address;
import ru.yandex.practicum.model.warehouse.Dimension;
import ru.yandex.practicum.model.warehouse.WarehouseProduct;
import ru.yandex.practicum.repository.AddressRepository;
import ru.yandex.practicum.repository.OrderRepository;
import ru.yandex.practicum.repository.WarehouseRepository;
import ru.yandex.practicum.utils.warehouse.AddressMapper;
import ru.yandex.practicum.utils.warehouse.WarehouseProductBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final AddressRepository addressRepository;
    private final WarehouseRepository warehouseRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final FeignClientOrder feignClientOrder;
    private final FeignClientDelivery feignClientDelivery;

    @Transactional
    public void registerNewProduct(NewProductInWarehouseRequest request) {
        UUID productId = request.getProductId();
        if (warehouseRepository.existsByProductId(productId)) {
            String userMessage = "Unable to register new product";
            log.warn("{} id={}", userMessage, productId);
            throw new SpecifiedProductAlreadyInWarehouseException(userMessage, productId);
        }
        WarehouseProduct warehouseProduct = WarehouseProductBuilder.buildWarehouseProduct(request);
        warehouseRepository.save(warehouseProduct);
    }

    @Transactional(readOnly = true)
    public BookedProductsDto checkProductQuantity(ShoppingCartDto shoppingCartDto) {
        final String userMessage = "Unable to check product quantity";

        Map<UUID, Integer> products = shoppingCartDto.getProducts();
        Set<UUID> productIds = products.keySet();

        // поиск товаров на складе по списку корзины
        List<WarehouseProduct> warehouseProductList = warehouseRepository.findByProductIdIn(productIds);
        Map<UUID, WarehouseProduct> productById = toMap(warehouseProductList);

        // список товаров, не найденных на складе
        List<UUID> productsNotFound = new ArrayList<>();

        // характеристика заказа (доставки)
        double deliveryWeight = 0.0; // начальное значение веса
        double deliveryVolume = 0.0; // начальное значение объема
        boolean deliveryFragile = false; // начальное значение хрупкости

        // итерация по списку id-товаров из корзины
        for (UUID productId : productIds) {
            Integer quantityExpected = products.get(productId); // желаемое количество товара

            if (quantityExpected == null) {
                String message = String.format("Expected quantity is null for product id=%s", productId);
                throw new IllegalArgumentException(message);
            }

            // пополнение списка не найденных товаров
            if (!productById.containsKey(productId)) {
                productsNotFound.add(productId);
                continue;
            }

            // получение текущего товара
            WarehouseProduct warehouseProduct = productById.get(productId);

            // доступное количество товара на складе
            Integer quantityAvailable = warehouseProduct.getQuantity();

            if (quantityExpected > quantityAvailable) {
                throw new ProductInShoppingCartLowQuantityInWarehouseException(userMessage, productId);
            }

            // объем единичного товара
            Double productVolume = getProductVolume(warehouseProduct, productId);

            // приращение объема единичного товара к характеристике заказа (доставке)
            deliveryVolume += productVolume * quantityExpected;

            // вес единичного товара
            Double productWeight = getProductWeight(warehouseProduct, productId);

            // приращение  веса единичного товара к характеристике заказа (доставке)
            deliveryWeight += productWeight * quantityExpected;

            //хрупкость товара
            boolean fragile = warehouseProduct.isFragile();

            // если заказ не хрупкий и хотя бы один товар хрупкий, то заказ становиться хрупким
            if (!deliveryFragile && fragile)
                deliveryFragile = true;
        }

        // если список товаров, не найденных на складе, имеет записи
        if (!productsNotFound.isEmpty()) {
            log.warn("{}. Products not found:{}", userMessage, productsNotFound);
            throw new NoSpecifiedProductInWarehouseException(userMessage, productsNotFound);
        }

        // данные по забронированным продуктам (заказу)
        return BookedProductsDto.builder()
                .fragile(deliveryFragile)
                .deliveryWeight(deliveryWeight)
                .deliveryVolume(deliveryVolume)
                .build();
    }

    @Transactional
    public void addProduct(@Valid AddProductToWarehouseRequest request) {
        UUID productId = request.getProductId();
        String errorMessage = "Unable to add product to warehouse";

        WarehouseProduct warehouseProduct = warehouseRepository.findByProductId(request.getProductId())
                .orElseThrow(() -> new WarehouseProductNotFoundException(errorMessage, productId));
        Integer quantity = request.getQuantity();
        warehouseProduct.setQuantity(quantity);
    }

    public Address getAddress() {
        String address = addressRepository.getAddress();
        return Address.builder()
                .country(address)
                .addressName(address)
                .city(address)
                .street(address)
                .house(address)
                .flat(address)
                .build();
    }

    @Transactional
    public List<WarehouseProduct> returnProductsToWarehouse(Map<UUID, Integer> products) {
        String userMessage = "Unable to return products to warehouse";

        // список id-товаров на возврат
        List<UUID> productIdsToReturn = products.keySet().stream().toList();

        // поиск товаров для возврата в хранилище
        List<WarehouseProduct> productsInWarehouse = warehouseRepository.findAllByProductIdIn(productIdsToReturn);
        List<UUID> productIdsInWarehouse = productsInWarehouse.stream().map(WarehouseProduct::getProductId).toList();

        // список товаров, не найденных на складе
        List<UUID> productsNotFound = productIdsToReturn.stream()
                .filter(productId -> !productIdsInWarehouse.contains(productId))
                .toList();

        // список товаров с обновленными данными по количеству после возврата
        List<WarehouseProduct> productsToUpdate = new ArrayList<>();

        // итерация по списку товаров, найденных на складе
        for (WarehouseProduct warehouseProduct : productsInWarehouse) {

            // обновление данных товаров по количеству после возврата
            UUID productId = warehouseProduct.getProductId();
            Integer quantityCurrent = warehouseProduct.getQuantity();
            Integer quantityToReturn = products.get(warehouseProduct.getProductId());
            Integer totalQuantity = quantityCurrent + quantityToReturn;
            warehouseProduct.setQuantity(totalQuantity);
            productsToUpdate.add(warehouseProduct);
        }

        // если список товаров, не найденных на складе, имеет записи
        if (!productsNotFound.isEmpty()) {
            log.warn("{}. Products not found:{}", userMessage, productsNotFound);
            throw new NoSpecifiedProductInWarehouseException(userMessage, productsNotFound);
        }

        // обновление товаров в репозитории
        return warehouseRepository.saveAll(productsToUpdate);
    }

    @Transactional
    public BookedProductsDto assembleProducts(AssemblyProductsForOrderRequest request) {
        UUID orderId = request.getOrderId();
        Map<UUID, Integer> productsToAssemble = request.getProducts();

        // изменение статуса заказа
        OrderDto orderDto = feignClientOrder.assembleByOrderId(orderId);

        // dto для запроса в checkProductQuantity
        ShoppingCartDto shoppingCartDto = ShoppingCartDto.builder()
                .products(productsToAssemble)
                .build();

        // проверка наличия товаров на складе
        BookedProductsDto bookedProducts = checkProductQuantity(shoppingCartDto);

        List<UUID> productIdsToAssemble = productsToAssemble.keySet().stream().toList();

        // поиск товаров на складе
        List<WarehouseProduct> productsInWarehouse = warehouseRepository.findAllByProductIdIn(productIdsToAssemble);

        List<WarehouseProduct> productsToUpdate = new ArrayList<>();

        // уменьшение количества товара на складе
        for (WarehouseProduct warehouseProduct : productsInWarehouse) {
            UUID productId = warehouseProduct.getProductId();
            Integer quantityToAssemble = productsToAssemble.get(productId);
            Integer quantityInWarehouse = warehouseProduct.getQuantity();
            Integer newQuantity = quantityInWarehouse - quantityToAssemble;
            warehouseProduct.setQuantity(newQuantity);
            productsToUpdate.add(warehouseProduct);
        }
        warehouseRepository.saveAll(productsToUpdate);
        return bookedProducts;
    }

    @Transactional
    public void shippingProducts(ShippedToDeliveryRequest request) {
        String userMessage = "Unable to ship product to delivery";

        UUID orderId = request.getOrderId();
        UUID deliveryId = request.getDeliveryId();

        // поиск заказа
        Order order = orderService.getOrderById(orderId, userMessage);

        // новый статус заказа
        OrderState newState = OrderState.ON_DELIVERY;

        // ожидаемый текущий статус заказа
        OrderState expectedState = OrderState.ASSEMBLED;

        if (order.getState() != OrderState.ASSEMBLED) {
            throw new ValidationException(
                    "Unable to change order status to: " + newState +
                            ". Expected state: " + expectedState +
                            ". Current state: " + order.getState());
        }

        // адрес получателя
        Address toAddress = order.getAddress();
        AddressDto toAddressDto = AddressMapper.toAddressDto(toAddress);

        // адрес склада
        Address fromAddress = getAddress();
        AddressDto fromAddressDto = AddressMapper.toAddressDto(fromAddress);

        // dto для запроса создания новой доставки в модуле delivery
        DeliveryDto deliveryDto = DeliveryDto.builder()
                .deliveryId(deliveryId)
                .fromAddress(fromAddressDto)
                .toAddress(toAddressDto)
                .orderId(orderId)
                .state(DeliveryState.CREATED)
                .build();

        // запрос на создание доставки в модуле delivery
        feignClientDelivery.createDeliveryByDto(deliveryDto);

        // обновление статуса заказа
        order.setState(newState);
        orderRepository.save(order);
    }

    private Map<UUID, WarehouseProduct> toMap(List<WarehouseProduct> warehouseProducts) {
        return warehouseProducts.stream()
                .collect(Collectors.toMap(WarehouseProduct::getProductId, product -> product));
    }

    private Double getProductVolume(WarehouseProduct warehouseProduct, UUID productId) {
        // получение характеристик товара
        Dimension dimension = warehouseProduct.getDimension();

        if (dimension == null) {
            String message = String.format("Dimension is null for product id=%s", productId);
            throw new IllegalArgumentException(message);
        }

        Double width = dimension.getWidth(); // ширина
        Double height = dimension.getHeight(); // высота
        Double depth = dimension.getDepth(); // глубина

        // определяем, какие из полей dimension равны null и добавляем их в list
        if (width == null || height == null || depth == null) {
            List<String> nullDimensionFields = new ArrayList<>();
            if (width == null) nullDimensionFields.add("width");
            if (height == null) nullDimensionFields.add("height");
            if (depth == null) nullDimensionFields.add("depth");

            String message = String.format("Dimension fields %s are null for product id=%s",
                    nullDimensionFields, productId);
            throw new IllegalArgumentException(message);
        }

        // объем единичного товара
        return width * height * depth;
    }

    private Double getProductWeight(WarehouseProduct warehouseProduct, UUID productId) {
        Double productWeight = warehouseProduct.getWeight();

        if (productWeight == null) {
            String message = String.format("Weight is null for product id=%s", productId);
            throw new IllegalArgumentException(message);
        }

        // вес единичного товара
        return productWeight;
    }

    public List<String> getAllAddresses() {
        return addressRepository.getAllAddresses();
    }
}
