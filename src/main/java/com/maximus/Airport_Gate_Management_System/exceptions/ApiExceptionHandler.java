package com.maximus.Airport_Gate_Management_System.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ApiRequestBaseException.class})
    public ResponseEntity<Object> handleException(ApiRequestBaseException e) {

        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        log.error("Exception occurred: {}. Details: {}",
                e.getClass().getSimpleName(),
                e.getMessage(),
                e);

        log.debug("Returning response with status: {}, message: {}",
                badRequest, e.getMessage());

        ApiException apiException = new ApiException(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        log.trace("Created ApiException response: {}", apiException);

        return new ResponseEntity<>(apiException, badRequest);
    }
}