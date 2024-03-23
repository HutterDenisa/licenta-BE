package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class NoAnuntFoundByUserIdException extends RuntimeException {
    private final HttpStatus httpStatus;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public NoAnuntFoundByUserIdException(HttpStatus httpStatus) {
        this(httpStatus, null);
    }

    public NoAnuntFoundByUserIdException(HttpStatus httpStatus, Throwable cause) {
        super("Nu sunt anunturi cu acest user id", cause);
        this.httpStatus = httpStatus;
    }
}