package com.invenco.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.invenco.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
	// LEFT JOIN FETCH to fetch everything using 1 query.
	// If FETCH is not used, hibernate does multiple queries to fetch data (Fire queries for each order) 
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.products WHERE o.customer.id = :customerId")
    List<Order> findByCustomerIdWithProducts(@Param("customerId") Long customerId);
}