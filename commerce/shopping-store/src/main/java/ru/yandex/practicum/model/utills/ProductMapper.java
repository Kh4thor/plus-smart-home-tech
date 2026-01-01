package ru.yandex.practicum.model.utills;

import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.model.ProductDto;

public class ProductMapper {

    public static Product toProduct(ProductDto productDto) {
        if (productDto == null) return null;
        return Product.builder()
                .productId(productDto.getProductId())
                .productName(productDto.getProductName())
                .description(productDto.getDescription())
                .imageSrc(productDto.getImageSrc())
                .quantityState(productDto.getQuantityState())
                .productState(productDto.getProductState())
                .productCategory(productDto.getProductCategory())
                .price(PriceConverter.toCents(productDto.getPrice()))
                .build();
    }

    public static ProductDto toProductDto(Product product) {
        if (product == null) return null;
        return ProductDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .imageSrc(product.getImageSrc())
                .quantityState(product.getQuantityState())
                .productState(product.getProductState())
                .productCategory(product.getProductCategory())
                .price(PriceConverter.toRoubles(product.getPrice()))
                .build();
    }
}
