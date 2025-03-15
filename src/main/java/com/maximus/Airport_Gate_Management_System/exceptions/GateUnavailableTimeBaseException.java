package com.maximus.Airport_Gate_Management_System.exceptions;

public class GateUnavailableTimeBaseException extends ApiRequestBaseException {

    public GateUnavailableTimeBaseException(String message) {
        super(message);
    }

    public GateUnavailableTimeBaseException(String message, Throwable cause) {
        super(message, cause);
    }

}
