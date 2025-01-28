package com.example.demo.dto.shoppingcart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemResponseDto {
    private Long cartItemId;
    private Long bookId;
    private String bookTitle;
    private int quantity;
}
