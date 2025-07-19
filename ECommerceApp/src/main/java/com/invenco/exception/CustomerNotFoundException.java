package com.invenco.exception;

public class CustomerNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 5209623001730956674L;

	public CustomerNotFoundException(String message) {
        super(message);
    }
}

