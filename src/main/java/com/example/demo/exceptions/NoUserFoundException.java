package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class NoUserFoundException extends RuntimeException{

    private final HttpStatus httpStatus;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public NoUserFoundException(HttpStatus httpStatus)
    {
        this(httpStatus,null);
    }

    public NoUserFoundException(HttpStatus httpStatus,Throwable cause)
    {
        super("There is no user with this id",cause);
        this.httpStatus=httpStatus;
    }
}
