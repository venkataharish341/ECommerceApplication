package com.invenco.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRequest {
    
    @NotBlank(message = "Product name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Product price is required")
    private BigDecimal price;
    
    @PositiveOrZero(message = "Stock quantity must be zero or positive")
    private Integer stockQuantity;
}
