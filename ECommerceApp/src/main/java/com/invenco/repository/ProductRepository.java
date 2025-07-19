package com.invenco.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.invenco.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
	// Using JPQL Query
    @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
    List<Product> findByIdIn(@Param("productIds") List<Long> productIds);
}