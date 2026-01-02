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
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public Product create(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    @Override
    public Product update(Product toUpdate) {
        UUID id = toUpdate.getProductId();
        Product current = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

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
        UUID id = toUpdate.getProductId();
        Product current = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        if (toUpdate.getQuantityState() != null) {
            current.setQuantityState(toUpdate.getQuantityState());
        }
        Product updated = productRepository.save(current);
        return true;
    }

    @Override
    public boolean remove(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException();
        }
        productRepository.deleteById(id);
        return true;
    }
}
