package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class NoAnuntFoundByIdException extends RuntimeException{
    private final HttpStatus httpStatus;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public NoAnuntFoundByIdException(HttpStatus httpStatus)
    {
        this(httpStatus,null);
    }

    public NoAnuntFoundByIdException(HttpStatus httpStatus,Throwable cause)
    { super("Nu sunt anunturi cu acest id",cause);
        this.httpStatus=httpStatus;
    }
}