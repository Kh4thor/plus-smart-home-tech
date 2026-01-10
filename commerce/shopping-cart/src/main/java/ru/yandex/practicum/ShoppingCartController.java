package ru.yandex.practicum;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.model.ShoppingCart;
import ru.yandex.practicum.service.ShoppingCartService;

@RestController
@RequestMapping("/api/v1/shopping-cart")
public class ShoppingCartController {


    ShoppingCartService shoppingCartService;

    @GetMapping
    public ShoppingCart getShoppingCart(@RequestParam String userName){
        return shoppingCartService.getShoppingCart(userName);
    }
}
