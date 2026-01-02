package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.repository.ProductRepository;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Override
    public Product getById(UUID id) {
        String userMessage = "Unable to get product by id";
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(userMessage, id));
    }

    @Override
    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    @Override
    public Product update(Product toUpdate) {
        String userMessage = "Unable to update product";
        UUID id = toUpdate.getProductId();
        Product current = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(userMessage, id));

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
        return productRepository.save(current);
    }

    @Transactional
    @Override
    public boolean updateQuantityState(Product toUpdate) {
        String userMessage = "Unable to update product quantity state";
        UUID id = toUpdate.getProductId();
        Product current = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(userMessage, id));
        if (toUpdate.getQuantityState() != null) {
            current.setQuantityState(toUpdate.getQuantityState());
        }
        Product updated = productRepository.save(current);
        return true;
    }

    @Override
    public boolean remove(UUID id) {
        if (!productRepository.existsById(id)) {
            String userMessage = "Unable to remove product by id";
            throw new ProductNotFoundException(userMessage, id);
        }
        productRepository.deleteById(id);
        return true;
    }
}
