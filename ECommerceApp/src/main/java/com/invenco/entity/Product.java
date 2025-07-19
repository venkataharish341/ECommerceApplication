package com.invenco.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "orders")
@ToString(exclude = "orders")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Product name is required")
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @NotNull(message = "Product price is required")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @PositiveOrZero(message = "Stock quantity must be zero or positive")
    @Column(nullable = false)
    private Integer stockQuantity;
    
    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
    
    public Product(String name, String description, BigDecimal price, Integer stockQuantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
    
    public boolean isInStock(Integer requestedQuantity) {
        return stockQuantity >= requestedQuantity;
    }
    
    public void deductStock(Integer quantity) {
        if (quantity > stockQuantity) {
            throw new IllegalArgumentException("Insufficient stock available");
        }
        this.stockQuantity -= quantity;
    }
}