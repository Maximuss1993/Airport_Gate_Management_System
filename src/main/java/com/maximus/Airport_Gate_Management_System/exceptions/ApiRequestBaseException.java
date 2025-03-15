package com.maximus.Airport_Gate_Management_System.exceptions;

public class ApiRequestBaseException extends RuntimeException {

    public ApiRequestBaseException(String message) {
        super(message);
    }

    public ApiRequestBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
