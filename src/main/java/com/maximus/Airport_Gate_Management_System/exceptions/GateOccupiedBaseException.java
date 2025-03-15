package com.maximus.Airport_Gate_Management_System.exceptions;

public class GateOccupiedBaseException extends ApiRequestBaseException {

    public GateOccupiedBaseException(String message) {
        super(message);
    }

    public GateOccupiedBaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
