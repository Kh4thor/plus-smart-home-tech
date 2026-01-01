package ru.yandex.practicum.service;

import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.model.ProductDto;
import ru.yandex.practicum.model.enums.ProductState;
import ru.yandex.practicum.model.enums.QuantityState;
import ru.yandex.practicum.repository.ProductRepository;

public class ProductService {

    private ProductRepository productRepository;

    public Product getById(String id) {
        ProductDto responseDto = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        return ProductDto.toProduct(responseDto);
    }

    public Product create(ProductDto productDto) {
        ProductDto responseDto = productRepository.save(productDto);
        return ProductDto.toProduct(responseDto);
    }

    public Product update(ProductDto toUpdate) {
        String id = toUpdate.getProductId();
        ProductDto productCur = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        String nameCur = productCur.getProductName();
        String descriptionCur = productCur.getDescription();
        String imgSrcCur = productCur.getImageSrc();
        QuantityState quantityStateCur = productCur.getQuantityState();
        ProductState productStateCur = productCur.getProductState();
        Float priceCur = productCur.getPrice();






        currentProduct.setProductName(product);







    }
}
