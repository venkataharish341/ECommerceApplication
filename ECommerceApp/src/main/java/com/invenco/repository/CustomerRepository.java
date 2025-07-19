package com.invenco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.invenco.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
	// This is using Query DSL (Method name generates query)
    boolean existsByEmail(String email);

}
