package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;

public class NoAnuntFoundByNameException extends RuntimeException{
    private final HttpStatus httpStatus;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public NoAnuntFoundByNameException(HttpStatus httpStatus)
    {
        this(httpStatus,null);
    }

    public NoAnuntFoundByNameException(HttpStatus httpStatus,Throwable cause)
    { super("Nu sunt anunturi cu acest nume",cause);
        this.httpStatus=httpStatus;
    }
}
