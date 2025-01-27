package com.example.demo.service.shoppingcart;

import com.example.demo.dto.shoppingcart.CartItemRequestDto;
import com.example.demo.dto.shoppingcart.ShoppingCartResponseDto;
import com.example.demo.dto.shoppingcart.UpdateCartItemRequestDto;
import com.example.demo.model.User;

public interface ShoppingCartService {
    ShoppingCartResponseDto getCartByUserId(Long userId);

    ShoppingCartResponseDto addBookToCart(Long userId, CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto updateCartItem(Long itemId, User user,
                                           UpdateCartItemRequestDto cartItemDto);

    ShoppingCartResponseDto removeItemFromCart(User user, Long itemId);
}
