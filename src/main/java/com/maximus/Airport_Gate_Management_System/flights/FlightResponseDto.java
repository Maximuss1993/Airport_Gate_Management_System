package com.maximus.Airport_Gate_Management_System.flights;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record FlightResponseDto(

        String flightNumber,

        LocalTime arrivingTime,

        LocalTime leavingTime

) {
}
