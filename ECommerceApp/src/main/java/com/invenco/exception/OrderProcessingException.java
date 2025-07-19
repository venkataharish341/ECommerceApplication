package com.invenco.exception;

public class OrderProcessingException extends RuntimeException {
    private static final long serialVersionUID = 5392898442387759070L;

	public OrderProcessingException(String message) {
        super(message);
    }
}
