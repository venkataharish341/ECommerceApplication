package com.invenco.exception;

public class InsufficientStockException extends RuntimeException {
    private static final long serialVersionUID = -6768239170289799352L;

	public InsufficientStockException(String message) {
        super(message);
    }
}
