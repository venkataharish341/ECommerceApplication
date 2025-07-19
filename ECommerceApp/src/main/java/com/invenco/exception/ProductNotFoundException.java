package com.invenco.exception;

public class ProductNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 6880640176069440602L;

	public ProductNotFoundException(String message) {
        super(message);
    }
}