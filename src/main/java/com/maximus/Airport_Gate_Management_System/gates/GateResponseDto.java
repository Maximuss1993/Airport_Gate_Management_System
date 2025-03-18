package com.maximus.Airport_Gate_Management_System.gates;

import com.maximus.Airport_Gate_Management_System.flights.Flight;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record GateResponseDto(

        String name,

        LocalTime openingTime,

        LocalTime closingTime,

        Flight flight

) {
}
