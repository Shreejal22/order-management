package com.shreejal.ordermanagement.service;

import com.shreejal.ordermanagement.dto.OrderRequest;
import com.shreejal.ordermanagement.entity.Order;
import com.shreejal.ordermanagement.entity.OrderStatus;
import com.shreejal.ordermanagement.exception.OrderNotFoundException;
import com.shreejal.ordermanagement.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order createOrder(OrderRequest request) {
        Order order = Order.builder()
                .itemName(request.getItemName())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .status(OrderStatus.CREATED)
                .build();
        return orderRepository.save(order);
    }

    public Order getOrder(UUID id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    public Order updateOrder(UUID id, OrderRequest request) {
        Order order = getOrder(id);
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update a cancelled order");
        }
        order.setItemName(request.getItemName());
        order.setQuantity(request.getQuantity());
        order.setPrice(request.getPrice());
        return orderRepository.save(order);
    }

    public Order cancelOrder(UUID id) {
        Order order = getOrder(id);
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order is already cancelled");
        }
        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    public Page<Order> getOrders(OrderStatus status, Pageable pageable) {
        if (status != null) {
            return orderRepository.findByStatus(status, pageable);
        }
        return orderRepository.findAll(pageable);
    }
}