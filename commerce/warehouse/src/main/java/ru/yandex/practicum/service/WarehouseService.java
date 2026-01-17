package ru.yandex.practicum.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.dto.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.dto.warehouse.AddressDto;
import ru.yandex.practicum.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.dto.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.yandex.practicum.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.exception.WarehouseProductNotFoundException;
import ru.yandex.practicum.model.Dimension;
import ru.yandex.practicum.model.WarehouseProduct;
import ru.yandex.practicum.repository.AddressRepository;
import ru.yandex.practicum.repository.WarehouseRepository;
import ru.yandex.practicum.utills.WarehouseProductBuilder;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final AddressRepository addressRepository;

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
        Map<UUID, WarehouseProduct> productById = warehouseRepository.findAllAsMapByIds(productIds);

        // список товаров, не найденных на складе
        List<UUID> productsNotFound = new ArrayList<>();

        // характеристика заказа (доставки)
        double deliveryWeight = 0.0;
        double deliveryVolume = 0.0;
        boolean deliveryFragile = false;

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

        WarehouseProduct warehouseProduct = warehouseRepository.findByProductId(request.getProductId()).orElseThrow(() -> {
            log.warn(errorMessage);
            return new WarehouseProductNotFoundException(errorMessage, productId);
        });
        Integer quantity = request.getQuantity();
        warehouseProduct.setQuantity(quantity);
    }

    public AddressDto getAddress(AddressDto addressDto) {
        String address = addressRepository.getAddress();
        return AddressDto.builder()
                .country(address)
                .city(address)
                .street(address)
                .house(address)
                .flat(address)
                .build();
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

    private Double getProductWeight(WarehouseProduct warehouseProduct, UUID  productId) {
        Double productWeight = warehouseProduct.getWeight();

        if (productWeight == null) {
            String message = String.format("Weight is null for product id=%s", productId);
            throw new IllegalArgumentException(message);
        }

        // вес единичного товара
        return productWeight;
    }
}
