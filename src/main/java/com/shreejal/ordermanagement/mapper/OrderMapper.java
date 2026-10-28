package com.shreejal.ordermanagement.mapper;

import com.shreejal.ordermanagement.dto.OrderResponse;
import com.shreejal.ordermanagement.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // Calculates total price automatically during mapping
    @Mapping(target = "totalPrice", expression = "java(order.getPrice().multiply(java.math.BigDecimal.valueOf(order.getQuantity())))")
    OrderResponse toResponse(Order order);

    List<OrderResponse> toResponseList(List<Order> orders);
}