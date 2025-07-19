package com.invenco.service;

import com.invenco.dto.CreateCustomerRequest;
import com.invenco.dto.CustomerResponse;
import com.invenco.entity.Customer;
import com.invenco.exception.CustomerNotFoundException;
import com.invenco.exception.DuplicateEmailException;
import com.invenco.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    
    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        log.info("Creating customer with email: {}", request.getEmail());
        
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Customer with email " + request.getEmail() + " already exists");
        }
        
        Customer customer = new Customer(request.getName(), request.getEmail());
        Customer savedCustomer = customerRepository.save(customer);
        
        log.info("Customer created successfully with ID: {}", savedCustomer.getId());
        return new CustomerResponse(savedCustomer.getId(), savedCustomer.getName(), savedCustomer.getEmail());
    }
    
    @Transactional(readOnly = true)
    public Customer findCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));
    }
}
