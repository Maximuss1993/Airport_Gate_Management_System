package com.maximus.Airport_Gate_Management_System.flights;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalTime;

@Builder
public record FlightResponseDto(

    String flightNumber,

    @JsonFormat(pattern = "HH:mm")
    LocalTime arrivingTime

) {
}
