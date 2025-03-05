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

        var flight = new Flight();
        flight.setFlightNumber(dto.flightNumber());
        flight.setArrivingDate(dto.arrivingDate());
        flight.setArrivingTime(dto.arrivingTime());

        return flight;
    }

    public FlightResponseDto toFlightResponseDto(Flight flight) {
        return new FlightResponseDto(
                flight.getFlightNumber(),
                flight.getArrivingDate(),
                flight.getArrivingTime()
        );
    }
}