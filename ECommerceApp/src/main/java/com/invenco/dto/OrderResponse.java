package com.invenco.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.invenco.entity.OrderStatus;

@Data
public class OrderResponse {
    private Long id;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private Boolean isPremium;
    private OrderStatus status;
    private CustomerResponse customer;
    private List<OrderItemResponse> items;
    
    public OrderResponse(Long id, LocalDateTime orderDate, BigDecimal totalAmount, 
                        Boolean isPremium, OrderStatus status, CustomerResponse customer, 
                        List<OrderItemResponse> items) {
        this.id = id;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.isPremium = isPremium;
        this.status = status;
        this.customer = customer;
        this.items = items;
    }
}
