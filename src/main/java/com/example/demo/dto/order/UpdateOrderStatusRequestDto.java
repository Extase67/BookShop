package com.example.demo.dto.order;

import com.example.demo.model.Order;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequestDto(
        @NotNull(message = "Status is required")
        Order.Status status) {
}
