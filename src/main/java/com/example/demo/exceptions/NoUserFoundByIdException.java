package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class NoUserFoundByIdException extends RuntimeException{
    private final HttpStatus httpStatus;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public NoUserFoundByIdException(HttpStatus httpStatus)
    {
        this(httpStatus,null);
    }

    public NoUserFoundByIdException(HttpStatus httpStatus,Throwable cause)
    { super("Nu sunt useri cu acest id",cause);
        this.httpStatus=httpStatus;
    }
}
