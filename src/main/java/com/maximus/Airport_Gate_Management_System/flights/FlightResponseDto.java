package com.maximus.Airport_Gate_Management_System.flights;

import java.time.LocalDate;
import java.time.LocalTime;

public record FlightResponseDto(

        String airline,

        LocalDate arrivingDate,

        LocalTime arrivingTime

) {
}
