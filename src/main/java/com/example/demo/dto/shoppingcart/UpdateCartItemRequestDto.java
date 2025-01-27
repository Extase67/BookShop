package com.example.demo.dto.shoppingcart;

import jakarta.validation.constraints.Positive;

public record UpdateCartItemRequestDto(
        @Positive(message = "quantity can't be less than 1")
        int quantity) {
}
