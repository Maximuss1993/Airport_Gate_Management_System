package com.maximus.Airport_Gate_Management_System.flights;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record FlightDto(

        @NotBlank(message = "Flight number should not be blank.")
        String flightNumber,

        @NotNull(message =
                "Arriving time of the flight should not be null.")
        LocalTime arrivingTime

) {
}
