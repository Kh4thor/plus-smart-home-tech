package ru.yandex.practicum.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.shopping.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.enums.shopping.store.ProductCategory;
import ru.yandex.practicum.exception.shopping.store.ProductNotFoundException;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> findByCategory(ProductCategory category, Pageable pageable) {
        return productRepository.findByProductCategoryAndRemovedFalse(category, pageable);
    }

    @Override
    public Product getById(UUID id) {
        String userMessage = "Unable to get product";
        return productRepository.findByProductIdAndRemovedFalse(id).orElseThrow(() -> {
            log.warn("{} id={}", userMessage, id);
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
            log.warn("{} id={}", userMessage, id);
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
        log.info("Product has been updated to: {}", current);
        return current;
    }

    @Override
    @Transactional
    public boolean updateQuantityState(SetProductQuantityStateRequest request) {
        String userMessage = "Unable to update product quantity state";
        UUID id = request.getProductId();
        Product current = productRepository.findByProductIdAndRemovedFalse(id)
                .orElseThrow(() -> {
                    log.warn("{} id={}", userMessage, id);
                    return new ProductNotFoundException(userMessage, id);
                });
        if (request.getQuantityState() != null) {
            current.setQuantityState(request.getQuantityState());
        }
        log.info("QuantityState has been updated for product id={} to {}",
                id, current.getQuantityState());
        return true;
    }

    @Override
    @Transactional
    public boolean remove(UUID id) {
        String userMessage = "Unable to remove product";
        Product current = productRepository.findByProductIdAndRemovedFalse(id).orElseThrow(() -> {
            log.warn("{} id={}", userMessage, id);
            return new ProductNotFoundException(userMessage, id);
        });

        current.setRemoved(true);
        log.info("Product has been removed id={}", id);
        return current.isRemoved();
    }
}