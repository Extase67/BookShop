package com.example.demo.controller;

import com.example.demo.dto.order.OrderItemsResponseDto;
import com.example.demo.dto.order.OrderResponseDto;
import com.example.demo.dto.order.PlaceOrderRequestDto;
import com.example.demo.dto.order.UpdateOrderStatusRequestDto;
import com.example.demo.model.User;
import com.example.demo.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Place a new order")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto placeOrder(Authentication authentication,
                                       @RequestBody
                                       @Valid
                                       PlaceOrderRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.placeOrder(user, requestDto);
    }

    @Operation(summary = "Get all orders for the authenticated user")
    @GetMapping
    public List<OrderResponseDto> getAllOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getAllOrders(user);
    }

    @Operation(summary = "Find order by ID")
    @GetMapping("/{orderId}/items")
    public OrderResponseDto findOrderById(Authentication authentication,
                                          @PathVariable
                                          @Positive
                                          Long orderId) {
        User user = (User) authentication.getPrincipal();
        return orderService.findOrderById(orderId, user);
    }

    @Operation(summary = "Find order item by order ID and item ID")
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemsResponseDto findOrderItemByIdAndOrderId(Authentication authentication,
                                                             @PathVariable
                                                             @Positive
                                                             Long orderId,
                                                             @Positive
                                                             @PathVariable Long itemId) {
        User user = (User) authentication.getPrincipal();
        return orderService.findOrderItemByIdAndOrderId(orderId, itemId, user);
    }

    @Operation(summary = "Update order status")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public OrderResponseDto updateOrderStatus(@Positive
                                              @PathVariable Long id,
                                              @RequestBody
                                              @Valid
                                              UpdateOrderStatusRequestDto requestDto) {
        return orderService.updateOrderStatus(id, requestDto);
    }
}
