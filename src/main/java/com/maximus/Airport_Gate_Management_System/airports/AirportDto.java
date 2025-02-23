package com.maximus.Airport_Gate_Management_System.airports;

import jakarta.validation.constraints.NotEmpty;

public record AirportDto(

        @NotEmpty(message = "Name of the airport should not be empty.")
        String name,

        @NotEmpty(message = "Location of the airport should not be empty.")
        String location

) {
}
