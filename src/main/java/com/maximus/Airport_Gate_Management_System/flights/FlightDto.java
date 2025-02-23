package com.maximus.Airport_Gate_Management_System.flights;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.time.LocalTime;

public record FlightDto(

        @NotEmpty(message = "Airline of the flight should not be empty.")
        String airline,

        @NotEmpty(message =
                "Arriving date of the flight should not be empty.")
        LocalDate arrivingDate,

        @NotEmpty(message =
                "Arriving time of the flight should not be empty.")
        LocalTime arrivingTime

) {
}
