package com.app.ies.security.auth.domain.exceptions;

public class AccessDeniedException extends DomainException {
    public AccessDeniedException(String message) {
        super(403, message);
    }
}
