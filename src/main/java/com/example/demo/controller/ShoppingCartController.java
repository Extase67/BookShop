package com.example.demo.controller;

import com.example.demo.dto.shoppingcart.CartItemRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.dto.shoppingcart.UpdateCartItemRequestDto;
import com.example.demo.model.User;
import com.example.demo.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "Get shopping cart", description = "Get shopping cart by user id")
    public ShoppingCartResponseDto getCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getCartByUserId(user.getId());
    }

    @Operation(summary = "Add book to cart", description = "Add book to cart by user id")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCartResponseDto addBookToCart(
            Authentication authentication,
            @Valid @RequestBody CartItemRequestDto cartItem) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addBookToCart(user.getId(), cartItem);
    }

    @Operation(summary = "Update cart item", description = "Update cart item by cart item id")
    @PutMapping("/items/{cartItemId}")
    public ShoppingCartResponseDto updateCartItem(
            Authentication authentication,
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequestDto cartItem) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateCartItem(cartItemId, user, cartItem);
    }

    @Operation(summary = "Remove cart item", description = "Remove cart item by cart item id")
    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ShoppingCartResponseDto removeCartItem(
            Authentication authentication,
            @PathVariable
            @Positive
            Long cartItemId) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.removeItemFromCart(user, cartItemId);
    }
}
