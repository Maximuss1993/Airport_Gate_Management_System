package com.maximus.Airport_Gate_Management_System.gates;

import lombok.Builder;

import java.time.LocalTime;

@Builder
public record GateResponseDto(

        String name,

        LocalTime openingTime,

        LocalTime closingTime
) {
}
