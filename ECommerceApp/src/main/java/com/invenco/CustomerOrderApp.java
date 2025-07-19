package com.invenco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CustomerOrderApp {
    public static void main(String[] args) {
        SpringApplication.run(CustomerOrderApp.class, args);
    }
}
