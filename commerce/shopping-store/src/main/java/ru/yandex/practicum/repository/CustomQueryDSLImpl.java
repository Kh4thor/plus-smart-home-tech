package ru.yandex.practicum.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.model.Pageable;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.model.QProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomQueryDSLImpl implements CustomQueryDSL {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Product> findByCategoryAndPageable(ProductCategory category, Pageable pageable) {
        final int pageDefaultValue = 0;
        final int sizeDefaultValue = 1;

        int page = pageable.getPage() == null ? pageDefaultValue : pageable.getPage();
        int size = pageable.getSize() == null ? sizeDefaultValue : pageable.getSize();
        int offset = page * size;
        List<String> sort = pageable.getSort();

        QProduct product = QProduct.product;
        BooleanExpression byCategory = product.productCategory.eq(category);

        var query = queryFactory
                .selectFrom(product)
                .where(byCategory)
                .limit(size)
                .offset(offset);

        if (sort != null && !sort.isEmpty()) {
            query.orderBy(sort)
        }


        if (sort != null && !sort.isEmpty()) {
            List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
            for (String param : sort) {
                String[] params =  param.split(",");



            }

        }


        OrderSpecifier orderSpecifier = new OrderSpecifier<>();

        List<UUID> productsIds = products.stream()
                .map(Product::getProductId)
                .toList();

        log.info("Got products {}", productsIds);
        return products;
    }
}