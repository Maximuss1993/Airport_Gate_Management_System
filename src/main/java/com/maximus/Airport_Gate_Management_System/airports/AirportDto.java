package com.maximus.Airport_Gate_Management_System.airports;

import jakarta.validation.constraints.NotBlank;

public record AirportDto(

        @NotBlank(message = "Name of the airport should not be blank.")
        String name,

        @NotBlank(message = "Location of the airport should not be blank.")
        String location

) {
}
