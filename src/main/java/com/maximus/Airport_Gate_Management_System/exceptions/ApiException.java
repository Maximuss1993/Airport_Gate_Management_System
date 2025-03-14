package com.maximus.Airport_Gate_Management_System.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApiException {

    private final String message;

    private final HttpStatus httpStatus;

    private final ZonedDateTime timestamp;

}
