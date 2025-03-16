package com.maximus.Airport_Gate_Management_System.exceptions;

public class GateOccupiedException extends ApiRequestBaseException {

    public GateOccupiedException(String message) {
        super(message);
    }

    public GateOccupiedException(String message, Throwable cause) {
        super(message, cause);
    }
}
