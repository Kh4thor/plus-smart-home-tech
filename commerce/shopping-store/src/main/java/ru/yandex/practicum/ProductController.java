package ru.yandex.practicum;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.SetProductQuantityStateRequest;
import ru.yandex.practicum.enums.ProductCategory;
import ru.yandex.practicum.model.Product;
import ru.yandex.practicum.service.ProductService;
import ru.yandex.practicum.utills.ProductMapper;

import java.util.List;
import java.util.UUID;

/**
 * REST контроллер для управления товарами в онлайн-магазине.
 * Предоставляет API для CRUD операций над товарами, включая пагинацию, фильтрацию по категориям
 * и управление статусами товаров.
 *
 * <p>Все эндпоинты доступны по базовому пути: {@code /api/v1/shopping-store}</p>
 *
 * @see Product
 * @see ProductDto
 * @see ProductService
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    /**
     * Получает пагинированный список товаров по указанной категории.
     * Поддерживает сортировку и пагинацию через параметр {@link Pageable}.
     *
     * @param category категория товаров для фильтрации (обязательный параметр)
     * @param pageable параметры пагинации и сортировки (обязательный параметр)
     * @return список {@link ProductDto} товаров, соответствующих критериям
     * @apiNote Пример запроса: {@code GET /api/v1/shopping-store?category=LIGHTING&page=0&size=20&sort=price,desc}
     * @see ProductCategory
     * @see Pageable
     * @see ProductDto
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getAllProducts(@RequestParam ProductCategory category, @RequestParam Pageable pageable) {
        log.info("API: GET /api/v1/shopping-store?category={}&page={}&size={}&sort={}",
                category,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort());
        return productService.findByCategory(category, pageable).stream()
                .map(ProductMapper::toProductDto)
                .toList();
    }

    /**
     * Создает новый товар в системе.
     * Принимает {@link ProductDto} с описанием товара, валидирует данные и сохраняет в БД.
     *
     * @param productDto DTO с данными нового товара (обязательный параметр)
     * @return {@link ProductDto} созданного товара с присвоенным ID
     * @throws jakarta.validation.ConstraintViolationException если данные не прошли валидацию
     * @apiNote Использует HTTP метод PUT для создания, что нестандартно для REST,
     * но соответствует спецификации проекта
     * @see ProductDto
     * @see Product
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto create(@RequestBody @Valid ProductDto productDto) {
        log.info("API: PUT /api/v1/shopping-store");
        log.info("Got productDto for creation: {}", productDto);
        Product product = ProductMapper.toProduct(productDto);
        Product created = productService.create(product);
        log.info("Product has been created: {}", created);
        return ProductMapper.toProductDto(created);
    }

    /**
     * Полностью обновляет существующий товар.
     * Заменяет все поля товара на значения из переданного {@link ProductDto}.
     *
     * @param productDto DTO с обновленными данными товара (обязательный параметр)
     * @return {@link ProductDto} обновленного товара
     * @throws ru.yandex.practicum.exception.ProductNotFoundException если товар с указанным ID не найден
     * @throws jakarta.validation.ConstraintViolationException        если данные не прошли валидацию
     * @apiNote Для частичного обновления используйте соответствующие эндпоинты
     * @see ProductDto
     * @see Product
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto update(@RequestBody @Valid ProductDto productDto) {
        log.info("API: POST /api/v1/shopping-store");
        log.info("Got productDto for total updating: {}", productDto);
        Product product = ProductMapper.toProduct(productDto);
        Product updated = productService.update(product);
        log.info("Product has been updated: {}", updated);
        return ProductMapper.toProductDto(updated);
    }

    /**
     * Удаляет товар из ассортимента магазина.
     *
     * @param productId UUID идентификатор товара для удаления (обязательный параметр)
     * @return {@code true} если товар успешно удален, {@code false} в случае ошибки
     * @throws ru.yandex.practicum.exception.ProductNotFoundException если товар с указанным ID не найден
     * @apiNote Использует HTTP метод POST для удаления, что нестандартно для REST,
     * но соответствует спецификации проекта
     * @see UUID
     */
    @PostMapping("/removeProductFromStore")
    @ResponseStatus(HttpStatus.OK)
    public boolean removeProductFromStore(@RequestBody UUID productId) {
        log.info("API: POST /api/v1/shopping-store/removeProductFromStore");
        log.info("Got UUID for removing: {}", productId);
        if (productService.remove(productId)) {
            log.info("Product has been removed: {}", productId);
            return true;
        }
        log.info("Product has not been removed: {}", productId);
        return false;
    }

    /**
     * Обновляет статус количества товара на складе.
     * Вызывается со стороны склада для обновления статусов "Закончился", "Мало", "Достаточно", "Много".
     *
     * @param request DTO с идентификатором товара и новым статусом количества (обязательный параметр)
     * @return {@code true} если статус успешно обновлен, {@code false} в случае ошибки
     * @throws ru.yandex.practicum.exception.ProductNotFoundException если товар с указанным ID не найден
     * @throws jakarta.validation.ConstraintViolationException        если данные не прошли валидацию
     * @apiNote Пример запроса:
     * {@code POST /api/v1/shopping-store/quantityState}
     * Body: {"productId": "uuid", "quantityState": "FEW"}
     * @see SetProductQuantityStateRequest
     * @see ru.yandex.practicum.enums.QuantityState
     */
    @PostMapping("/quantityState")
    @ResponseStatus(HttpStatus.OK)
    public boolean updateQuantityState(@RequestBody @Valid SetProductQuantityStateRequest request) {
        log.info("API: POST /api/v1/shopping-store/quantityState");
        log.info("Got productDto for updating quantity state: {}", request);
        if (productService.updateQuantityState(request)) {
            log.info("Product quantity state has been updated: {}", request);
            return true;
        }
        return false;
    }

    /**
     * Получает детальную информацию о товаре по его идентификатору.
     *
     * @param productId UUID идентификатор товара (обязательный параметр пути)
     * @return {@link ProductDto} с полной информацией о товаре
     * @throws ru.yandex.practicum.exception.ProductNotFoundException если товар с указанным ID не найден
     * @apiNote Пример запроса: {@code GET /api/v1/shopping-store/{productId}}
     * @see ProductDto
     * @see UUID
     */
    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getProduct(@PathVariable UUID productId) {
        log.debug("API: GET /api/v1/shopping-store/{}", productId);
        Product product = productService.getById(productId);
        log.debug("Product has been got: {}", product);
        return ProductMapper.toProductDto(product);
    }
}