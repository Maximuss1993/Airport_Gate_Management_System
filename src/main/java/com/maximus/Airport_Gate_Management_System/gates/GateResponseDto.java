package com.maximus.Airport_Gate_Management_System.gates;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maximus.Airport_Gate_Management_System.flights.Flight;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record GateResponseDto(

        String name,

        @JsonFormat(pattern = "HH:mm")
        LocalTime openingTime,

        @JsonFormat(pattern = "HH:mm")
        LocalTime closingTime,

        Flight flight

) {
}
