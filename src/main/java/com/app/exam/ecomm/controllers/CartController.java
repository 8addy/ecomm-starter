package com.app.exam.ecomm.controllers;

import com.app.exam.ecomm.controllers.dto.AddToCartRequest;
import com.app.exam.ecomm.models.entity.Cart;
import com.app.exam.ecomm.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<Optional<Cart>> gatCart() {
        Optional<Cart> cart = cartService.getCart();
        return ResponseEntity.ok(cart);
    }

    @PostMapping(value = {"/", "/{id}"})
    public ResponseEntity<?> addToCart(@PathVariable(name = "id", required = false) Integer id, @RequestBody AddToCartRequest request) {
        return cartService.addToCart(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArticleCart(@PathVariable(name = "id") Integer id) {
        return cartService.deleteArticleCart(id);
    }
}
