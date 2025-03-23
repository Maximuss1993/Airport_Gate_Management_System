package com.maximus.Airport_Gate_Management_System.gates;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record GateDto(

        @NotBlank(message = "Name of the gate should not be blank.")
        String name,

        @NotNull(message = "Opening time for the gate should not be null.")
        LocalTime openingTime,

        @NotNull(message = "Closing time for the gate should not be null.")
        LocalTime closingTime
) {
}
