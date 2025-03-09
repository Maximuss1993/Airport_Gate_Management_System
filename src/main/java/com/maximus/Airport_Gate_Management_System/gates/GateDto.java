package com.maximus.Airport_Gate_Management_System.gates;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record GateDto(

        @NotEmpty(message = "Name of the gate should not be empty.")
        String name,

        @NotNull(message = "Opening time for the gate should not be null.")
        LocalTime openingTime,

        @NotNull(message = "Closing time for the gate should not be null.")
        LocalTime closingTime
) {
}
