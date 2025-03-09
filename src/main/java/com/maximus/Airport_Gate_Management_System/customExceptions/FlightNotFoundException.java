package com.maximus.Airport_Gate_Management_System.customExceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlightNotFoundException extends RuntimeException {
    private static final Logger log = LoggerFactory.getLogger(
            FlightNotFoundException.class);

    public FlightNotFoundException(String message) {
        super(message);
        log.error(message);
    }
}