package com.example.demo.controller;

import com.example.demo.dto.shoppingcart.CartItemRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.service.shoppingcart.ShoppingCartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ShoppingCartResponseDto getCart(@RequestParam Long userId) {
        return shoppingCartService.getCartByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCartResponseDto addBookToCart(
            @RequestParam Long userId,
            @Valid @RequestBody CartItemRequestDto cartItemRequestDto) {
        return shoppingCartService.addBookToCart(userId, cartItemRequestDto);
    }

    @PutMapping("/items/{cartItemId}")
    public ShoppingCartResponseDto updateCartItem(
            @PathVariable Long cartItemId,
            @Valid @RequestBody CartItemRequestDto cartItemRequestDto) {
        return shoppingCartService.updateCartItem(cartItemId, cartItemRequestDto.quantity());
    }

    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCartItem(@PathVariable Long cartItemId) {
        shoppingCartService.removeCartItem(cartItemId);
    }
}
