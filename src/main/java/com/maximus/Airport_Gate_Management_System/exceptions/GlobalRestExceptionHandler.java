package com.maximus.Airport_Gate_Management_System.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalRestExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Object> handleRestRuntimeException(RuntimeException ex) {

    log.debug("RuntimeException in RestController occurred: {}. Details: {}",
        ex.getClass().getSimpleName(),
        ex.getMessage(),
        ex);

    HttpStatus serverErrorStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    var apiException = new ApiException(
        ex.getMessage(),
        serverErrorStatus,
        ZonedDateTime.now(ZoneId.of("Z"))
    );

    log.debug("Created response for RuntimeException occurred in " +
        "RestController: {}", apiException);

    return new ResponseEntity<>(apiException, serverErrorStatus);
  }

}