package com.shreejal.ordermanagement.repository;

import com.shreejal.ordermanagement.entity.Order;
import com.shreejal.ordermanagement.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
}