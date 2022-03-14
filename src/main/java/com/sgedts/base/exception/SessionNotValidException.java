package com.sgedts.base.exception;

import org.springframework.http.HttpStatus;

public class SessionNotValidException extends RuntimeException {
    public SessionNotValidException() {
        super(HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    public SessionNotValidException(String message) {
        super(message);
    }
}
