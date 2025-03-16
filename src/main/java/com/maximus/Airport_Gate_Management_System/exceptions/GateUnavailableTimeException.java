package com.maximus.Airport_Gate_Management_System.exceptions;

public class GateUnavailableTimeException extends ApiRequestBaseException {

    public GateUnavailableTimeException(String message) {
        super(message);
    }

    public GateUnavailableTimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
