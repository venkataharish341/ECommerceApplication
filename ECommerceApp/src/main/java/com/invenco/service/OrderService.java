package com.invenco.service;

import com.invenco.dto.*;
import com.invenco.entity.*;
import com.invenco.exception.*;
import com.invenco.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final ProductService productService;
    
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("Creating order for customer ID: {}", request.getCustomerId());
        
        // Find customer
        Customer customer = customerService.findCustomerById(request.getCustomerId());
        
        // Find products
        List<Long> productIds = request.getItems().stream()
                .map(OrderItemRequest::getProductId)
                .collect(Collectors.toList());
        
        List<Product> products = productService.findProductsByIds(productIds);
        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        
        // Create order
        Order order = new Order(customer);
        
        try {
            // Check stock and add products to order
            for (OrderItemRequest item : request.getItems()) {
                Product product = productMap.get(item.getProductId());
                
                if (!product.isInStock(item.getQuantity())) {
                    throw new InsufficientStockException(
                        "Insufficient stock for product " + product.getName() + 
                        ". Available: " + product.getStockQuantity() + 
                        ", Requested: " + item.getQuantity());
                }
                
                // Deduct stock
                product.deductStock(item.getQuantity());
                
                // Add product to order
                order.addProduct(product, item.getQuantity());
            }
            
            // Save order
            Order savedOrder = orderRepository.save(order);
            
            log.info("Order created successfully with ID: {}, Total: {}, Premium: {}", 
                    savedOrder.getId(), savedOrder.getTotalAmount(), savedOrder.getIsPremium());
            
            return convertToOrderResponse(savedOrder);
            
        } catch (Exception e) {
            log.error("Error creating order: {}", e.getMessage());
            throw new OrderProcessingException("Failed to create order: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByCustomerId(Long customerId) {
        log.info("Fetching orders for customer ID: {}", customerId);
        
        // Verify customer exists
        customerService.findCustomerById(customerId);
        
        List<Order> orders = orderRepository.findByCustomerIdWithProducts(customerId);
        return orders.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }
    
    private OrderResponse convertToOrderResponse(Order order) {
        CustomerResponse customerResponse = new CustomerResponse(
                order.getCustomer().getId(),
                order.getCustomer().getName(),
                order.getCustomer().getEmail()
        );
        
        List<OrderItemResponse> items = order.getProducts().stream()
                .map(product -> {
                    Integer quantity = order.getQuantityForProduct(product);
                    BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
                    return new OrderItemResponse(
                            product.getId(),
                            product.getName(),
                            quantity,
                            product.getPrice(),
                            subtotal
                    );
                })
                .collect(Collectors.toList());
        
        return new OrderResponse(
                order.getId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getIsPremium(),
                order.getStatus(),
                customerResponse,
                items
        );
    }
}
