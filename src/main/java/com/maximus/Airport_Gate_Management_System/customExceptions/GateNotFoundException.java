package com.maximus.Airport_Gate_Management_System.customExceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GateNotFoundException extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(
            GateNotFoundException.class);

    public GateNotFoundException(String message) {
        super(message);
        log.error(message);
    }
}
