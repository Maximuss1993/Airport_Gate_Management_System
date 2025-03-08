package com.maximus.Airport_Gate_Management_System.airports;

import lombok.Builder;

@Builder
public record AirportResponseDto(

        String name,

        String location

) {
}
