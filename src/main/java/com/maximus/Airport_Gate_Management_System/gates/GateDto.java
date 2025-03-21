package com.maximus.Airport_Gate_Management_System.gates;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record GateDto(

        @NotEmpty(message = "Name of the gate should not be empty.")
        @NotNull(message = "Name of the gate should not be null.")
        String name,

        @NotNull(message = "Opening time for the gate should not be null.")
        LocalTime openingTime,

        @NotNull(message = "Closing time for the gate should not be null.")
        LocalTime closingTime
) {
}
