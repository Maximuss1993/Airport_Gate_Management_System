package com.maximus.Airport_Gate_Management_System.flights;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FlightMapper {

    public Flight toFlight(FlightDto dto) {

        if (dto == null) {

            log.error("The flight DTO is null. Throwing NullPointerException.");

            throw new NullPointerException("The flight DTO should not be null!");
        }

        return Flight.builder()
                .flightNumber(dto.flightNumber())
                .arrivingTime(dto.arrivingTime())
                .leavingTime(dto.leavingTime())
                .build();
    }

    public FlightResponseDto toFlightResponseDto(Flight flight) {
        return FlightResponseDto.builder()
                .flightNumber(flight.getFlightNumber())
                .arrivingTime(flight.getArrivingTime())
                .leavingTime(flight.getLeavingTime())
                .build();
    }
}