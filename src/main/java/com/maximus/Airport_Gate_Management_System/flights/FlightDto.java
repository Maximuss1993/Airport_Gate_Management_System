package com.maximus.Airport_Gate_Management_System.flights;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.time.LocalTime;

public record FlightDto(

        @NotEmpty(message = "Flight number should not be empty.")
        String flightNumber,

        @NotEmpty(message =
                "Arriving time of the flight should not be empty.")
        LocalTime arrivingTime,

        @NotEmpty(message =
                "Leaving time of the flight should not be empty.")
                LocalTime leavingTime

) {
}
