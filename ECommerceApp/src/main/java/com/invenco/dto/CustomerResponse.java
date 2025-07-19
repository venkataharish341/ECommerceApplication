package com.invenco.dto;

import lombok.Data;

@Data
public class CustomerResponse {
    private Long id;
    private String name;
    private String email;
    
    public CustomerResponse(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}