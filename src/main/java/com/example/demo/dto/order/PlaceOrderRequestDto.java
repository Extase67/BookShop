package com.example.demo.dto.order;

import jakarta.validation.constraints.NotBlank;

public record PlaceOrderRequestDto(
        @NotBlank(message = "Address is required")
        String shippingAddress) {
}
