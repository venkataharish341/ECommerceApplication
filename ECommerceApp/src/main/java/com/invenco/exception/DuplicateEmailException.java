package com.invenco.exception;

public class DuplicateEmailException extends RuntimeException {
    private static final long serialVersionUID = 7622302258006630751L;

	public DuplicateEmailException(String message) {
        super(message);
    }
}