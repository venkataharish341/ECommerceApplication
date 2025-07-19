package com.invenco.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"customer", "products"})
@ToString(exclude = {"customer", "products"})
public class Order {
	
	private static final String PREMIUM_VALUE = "500.00";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;
    
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(name = "is_premium", nullable = false)
    private Boolean isPremium = false;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "order_products",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "order_product_quantities", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyJoinColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<Product, Integer> productQuantities = new HashMap<>();
    
    public Order(Customer customer) {
        this.customer = customer;
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
        this.totalAmount = BigDecimal.ZERO;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
        this.isPremium = totalAmount.compareTo(new BigDecimal(PREMIUM_VALUE)) > 0;
    }
    
    public void addProduct(Product product, Integer quantity) {
        if (!products.contains(product)) {
            products.add(product);
        }
        productQuantities.put(product, quantity);
        calculateTotalAmount();
    }
    
    public void calculateTotalAmount() {
        this.totalAmount = productQuantities.entrySet().stream()
            .map(entry -> {
                BigDecimal price = entry.getKey().getPrice();
                return price != null ? price.multiply(BigDecimal.valueOf(entry.getValue())) : BigDecimal.ZERO;
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.isPremium = totalAmount.compareTo(new BigDecimal(PREMIUM_VALUE)) > 0;
    }
    
    public Integer getQuantityForProduct(Product product) {
        return productQuantities.getOrDefault(product, 0);
    }
}