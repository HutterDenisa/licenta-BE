package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class NoAnuntFoundByTipAnuntException extends RuntimeException {
    private final HttpStatus httpStatus;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public NoAnuntFoundByTipAnuntException(HttpStatus httpStatus)
    {
        this(httpStatus,null);
    }

    public NoAnuntFoundByTipAnuntException(HttpStatus httpStatus,Throwable cause)
    { super("Nu sunt anunturi cu aces",cause);
        this.httpStatus=httpStatus;
    }
}
