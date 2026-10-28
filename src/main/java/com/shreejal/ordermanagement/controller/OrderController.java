package com.shreejal.ordermanagement.controller;

import com.shreejal.ordermanagement.dto.OrderRequest;
import com.shreejal.ordermanagement.dto.OrderResponse;
import com.shreejal.ordermanagement.entity.Order;
import com.shreejal.ordermanagement.entity.OrderStatus;
import com.shreejal.ordermanagement.mapper.OrderMapper;
import com.shreejal.ordermanagement.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        Order order = orderService.createOrder(request);

        // REST Best Practice: Return 201 Created with Location Header
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(order.getId())
                .toUri();

        return ResponseEntity.created(location).body(orderMapper.toResponse(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID id) {
        Order order = orderService.getOrder(id);
        return ResponseEntity.ok(orderMapper.toResponse(order));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable UUID id,
                                                     @Valid @RequestBody OrderRequest request) {
        Order order = orderService.updateOrder(id, request);
        return ResponseEntity.ok(orderMapper.toResponse(order));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable UUID id) {
        Order order = orderService.cancelOrder(id);
        return ResponseEntity.ok(orderMapper.toResponse(order));
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getOrders(
            @RequestParam(required = false) OrderStatus status,
            Pageable pageable) {

        Page<Order> orders = orderService.getOrders(status, pageable);
        Page<OrderResponse> response = orders.map(orderMapper::toResponse);
        return ResponseEntity.ok(response);
    }
}