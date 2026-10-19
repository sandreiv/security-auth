package com.app.ies.security.auth.domain.exceptions;

public class UnauthorizedException extends DomainException {
    public UnauthorizedException(String message) {
        super(401, message);
    }
}
