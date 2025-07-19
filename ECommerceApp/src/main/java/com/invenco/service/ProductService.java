package com.invenco.service;

import com.invenco.dto.CreateProductRequest;
import com.invenco.dto.ProductResponse;
import com.invenco.entity.Product;
import com.invenco.exception.ProductNotFoundException;
import com.invenco.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    
    private final ProductRepository productRepository;
    
    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse createProduct(CreateProductRequest request) {
        log.info("Creating product with name: {}", request.getName());
        
        Product product = new Product(request.getName(), request.getDescription(), 
                                    request.getPrice(), request.getStockQuantity());
        Product savedProduct = productRepository.save(product);
        
        log.info("Product created successfully with ID: {}", savedProduct.getId());
        
        return new ProductResponse(savedProduct.getId(), savedProduct.getName(), 
                                 savedProduct.getDescription(), savedProduct.getPrice(), 
                                 savedProduct.getStockQuantity());
    }
    
    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "'all'")
    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll().stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), 
                                                  product.getDescription(), product.getPrice(), 
                                                  product.getStockQuantity()))
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<Product> findProductsByIds(List<Long> productIds) {
        List<Product> products = productRepository.findByIdIn(productIds);
        if (products.size() != productIds.size()) {
            throw new ProductNotFoundException("One or more products not found");
        }
        return products;
    }
}
