package com.app.ies.security.auth.domain.exceptions;

public class DomainException extends RuntimeException {

    private final int statusCode;

    public DomainException(int statusCoe, String message) {
        super(message);
        this.statusCode = statusCoe;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
