package com.shreejal.ordermanagement.dto;

import com.shreejal.ordermanagement.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OrderResponse {
    private UUID id;
    private String itemName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;
}