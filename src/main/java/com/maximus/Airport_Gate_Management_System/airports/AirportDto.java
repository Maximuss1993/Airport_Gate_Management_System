package com.maximus.Airport_Gate_Management_System.airports;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AirportDto(

        @NotEmpty(message = "Name of the airport should not be empty.")
        @NotNull(message = "Name of the airport cannot be null.")
        String name,

        @NotEmpty(message = "Location of the airport should not be empty.")
        @NotNull(message = "Location of the airport cannot be null.")
        String location

) {
}
