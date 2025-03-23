package com.maximus.Airport_Gate_Management_System.exceptions;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            GateOccupiedException.class,
            GateUnavailableTimeException.class})
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {

        log.debug("RuntimeException occurred: {}. Details: {}",
                ex.getClass().getSimpleName(), ex.getMessage(), ex);

        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        var apiException = new ApiException(
                ex.getMessage(), badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        log.trace("Created response for RuntimeException: {}", apiException);

        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = {
            GateNotFoundException.class,
            FlightNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFoundException(
            EntityNotFoundException ex) {

        log.debug("EntityNotFoundException occurred: {}. Details: {}",
                ex.getClass().getSimpleName(), ex.getMessage(), ex);

        HttpStatus notFound = HttpStatus.NOT_FOUND;

        var apiException = new ApiException(
                ex.getMessage(), notFound,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        log.trace("Created response for EntityNotFoundException: {}", apiException);

        return new ResponseEntity<>(apiException, notFound);
    }
}