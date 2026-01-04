package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.model.Pageable;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ProductRepository;
import ru.yandex.practicum.utills.ProductMapper;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Override
    public List<ProductDto> findByCategoryAndPageable(ProductCategory category, Pageable pageable) {
        return productRepository.findByCategoryAndPageable(category, pageable).stream()
                .map(ProductMapper::toProductDto)
                .toList();
    }

    @Override
    public Product getById(UUID id) {
        String userMessage = "Unable to get product by id";
        return productRepository.findById(id).orElseThrow(() -> {
            log.warn("Unable to get product by id={}", id);
            return new ProductNotFoundException(userMessage, id);
        });
    }

    @Override
    public Product create(Product product) {
        Product created = productRepository.save(product);
        log.info("Product has been created: {}", created);
        return created;
    }


    @Override
    @Transactional
    public Product update(Product toUpdate) {
        String userMessage = "Unable to update product";
        UUID id = toUpdate.getProductId();
        Product current = productRepository.findById(id).orElseThrow(() -> {
            log.warn("Unable to update product={}", id);
            return new ProductNotFoundException(userMessage, id);
        });

        if (toUpdate.getProductName() != null) {
            current.setProductName(toUpdate.getProductName());
        }
        if (toUpdate.getDescription() != null) {
            current.setDescription(toUpdate.getDescription());
        }
        if (toUpdate.getImageSrc() != null) {
            current.setImageSrc(toUpdate.getImageSrc());
        }
        if (toUpdate.getPrice() != null) {
            current.setPrice(toUpdate.getPrice());
        }
        if (toUpdate.getQuantityState() != null) {
            current.setQuantityState(toUpdate.getQuantityState());
        }
        if (toUpdate.getProductState() != null) {
            current.setProductState(toUpdate.getProductState());
        }
        if (toUpdate.getProductCategory() != null) {
            current.setProductCategory(toUpdate.getProductCategory());
        }

        Product updated = productRepository.save(current);
        log.info("Product has been updated to: {}", updated);
        return updated;
    }

    @Override
    @Transactional
    public boolean updateQuantityState(Product toUpdate) {
        String userMessage = "Unable to update product quantity state";
        UUID id = toUpdate.getProductId();
        Product current = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(userMessage, id));
        if (toUpdate.getQuantityState() != null) {
            current.setQuantityState(toUpdate.getQuantityState());
        }
        Product updated = productRepository.save(current);
        log.info("QuantityState has been updated to: {}", updated.getQuantityState());
        return true;
    }

    @Override
    public boolean remove(UUID id) {
        if (!productRepository.existsById(id)) {
            String userMessage = "Unable to remove product by id";
            throw new ProductNotFoundException(userMessage, id);
        }
        productRepository.deleteById(id);
        log.info("Product has been removed id:{}", id);
        return true;
    }
}
