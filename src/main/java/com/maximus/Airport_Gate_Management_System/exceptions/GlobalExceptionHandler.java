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

    HttpStatus badRequestStatus = HttpStatus.BAD_REQUEST;

    log.debug("RuntimeException occurred: {}. Details: {}",
        ex.getClass().getSimpleName(),
        ex.getMessage(),
        ex);

    ApiException apiException = new ApiException(
        ex.getMessage(),
        badRequestStatus,
        ZonedDateTime.now(ZoneId.of("Z"))
    );

    log.debug("Created response for RuntimeException: {}", apiException);

    return new ResponseEntity<>(apiException, badRequestStatus);
  }

  @ExceptionHandler(value = {
      GateNotFoundException.class,
      FlightNotFoundException.class})
  public ResponseEntity<Object> handleEntityNotFoundException(
      EntityNotFoundException ex) {

    HttpStatus notFoundStatus = HttpStatus.NOT_FOUND;

    log.debug("EntityNotFoundException occurred: {}. Details: {}",
        ex.getClass().getSimpleName(),
        ex.getMessage(),
        ex);

    ApiException apiException = new ApiException(
        ex.getMessage(),
        notFoundStatus,
        ZonedDateTime.now(ZoneId.of("Z"))
    );

    log.debug("Created response for EntityNotFoundException: {}",
        apiException);

    return new ResponseEntity<>(apiException, notFoundStatus);
  }
}