package com.example.demo.dto.order;

import com.example.demo.model.Order;
import jakarta.validation.constraints.NotBlank;

public record UpdateOrderStatusRequestDto(
        @NotBlank(message = "Status is required")
        Order.Status status) {
}
