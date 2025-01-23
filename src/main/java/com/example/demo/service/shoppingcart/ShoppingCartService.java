package com.example.demo.service.shoppingcart;

import com.example.demo.dto.shoppingcart.CartItemRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getCartByUserId(Long userId);

    ShoppingCartResponseDto addBookToCart(Long userId, CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto updateCartItem(Long cartItemId, int quantity);

    void removeCartItem(Long cartItemId);
}
