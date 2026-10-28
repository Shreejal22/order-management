package com.shreejal.order_management.service;

import com.shreejal.ordermanagement.dto.OrderRequest;
import com.shreejal.ordermanagement.entity.Order;
import com.shreejal.ordermanagement.entity.OrderStatus;
import com.shreejal.ordermanagement.exception.OrderNotFoundException;
import com.shreejal.ordermanagement.repository.OrderRepository;
import com.shreejal.ordermanagement.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_shouldSaveWithStatusCreated() {
        OrderRequest request = new OrderRequest();
        request.setItemName("Laptop");
        request.setQuantity(2);
        request.setPrice(BigDecimal.valueOf(1500));

        Order saved = new Order();
        saved.setId(UUID.randomUUID());
        saved.setItemName("Laptop");
        saved.setQuantity(2);
        saved.setPrice(BigDecimal.valueOf(1500));
        saved.setStatus(OrderStatus.CREATED);

        when(orderRepository.save(any(Order.class))).thenReturn(saved);

        Order result = orderService.createOrder(request);

        assertThat(result.getStatus()).isEqualTo(OrderStatus.CREATED);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void getOrder_shouldThrowWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrder(id))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    void cancelOrder_shouldSetStatusCancelled() {
        UUID id = UUID.randomUUID();
        Order order = new Order();
        order.setId(id);
        order.setStatus(OrderStatus.CREATED);

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order cancelled = orderService.cancelOrder(id);

        assertThat(cancelled.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    void updateOrder_shouldThrowWhenCancelled() {
        UUID id = UUID.randomUUID();
        Order order = new Order();
        order.setId(id);
        order.setStatus(OrderStatus.CANCELLED);

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        OrderRequest request = new OrderRequest();
        request.setItemName("New");
        request.setQuantity(1);
        request.setPrice(BigDecimal.TEN);

        assertThatThrownBy(() -> orderService.updateOrder(id, request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("cancelled");
    }


    @Test
    void getOrders_shouldReturnPaginatedResults() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setStatus(OrderStatus.CREATED);

        Page<Order> mockPage = new PageImpl<>(List.of(order), pageable, 1);

        // Mock both the filtered and unfiltered repository calls
        when(orderRepository.findByStatus(OrderStatus.CREATED, pageable)).thenReturn(mockPage);
        when(orderRepository.findAll(pageable)).thenReturn(mockPage);

        // Act & Assert (With Filter)
        Page<Order> filteredResult = orderService.getOrders(OrderStatus.CREATED, pageable);
        assertThat(filteredResult.getTotalElements()).isEqualTo(1);
        assertThat(filteredResult.getContent().get(0).getStatus()).isEqualTo(OrderStatus.CREATED);

        // Act & Assert (Without Filter)
        Page<Order> allResult = orderService.getOrders(null, pageable);
        assertThat(allResult.getTotalElements()).isEqualTo(1);

        // Verify interactions
        verify(orderRepository).findByStatus(OrderStatus.CREATED, pageable);
        verify(orderRepository).findAll(pageable);
    }
}