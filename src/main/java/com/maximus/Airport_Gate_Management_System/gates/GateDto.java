package com.maximus.Airport_Gate_Management_System.gates;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalTime;

public record GateDto(
        @NotEmpty(message = "Name of the gate should not be empty.")
        String name
) {
}
