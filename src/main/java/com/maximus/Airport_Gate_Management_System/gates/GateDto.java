package com.maximus.Airport_Gate_Management_System.gates;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record GateDto(

        @NotBlank(message = "Name of the gate should not be blank.")
        String name,

        @NotNull(message = "Opening time for the gate should not be null.")
        @JsonFormat(pattern = "HH:mm")
        LocalTime openingTime,

        @NotNull(message = "Closing time for the gate should not be null.")
        @JsonFormat(pattern = "HH:mm")
        LocalTime closingTime
) {
}
