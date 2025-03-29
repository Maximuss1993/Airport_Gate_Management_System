package com.maximus.Airport_Gate_Management_System.exceptions;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ApiException(

    String message,

    HttpStatus httpStatus,

    ZonedDateTime timestamp) {
}
