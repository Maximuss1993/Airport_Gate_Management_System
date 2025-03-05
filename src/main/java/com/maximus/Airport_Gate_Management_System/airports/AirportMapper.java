package com.maximus.Airport_Gate_Management_System.airports;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AirportMapper {

    public Airport toAirport(AirportDto dto) {

        if (dto == null) {
            log.error("The airport DTO is null. Throwing NullPointerException.");
            throw new NullPointerException("The airport DTO should not be null!");
        }
        var airport = new Airport();
        airport.setName(dto.name());
        airport.setLocation(dto.location());

        return  airport;
    }

    public AirportResponseDto toAirportResponseDto(Airport airport) {
        return new AirportResponseDto(
                airport.getName(),
                airport.getLocation()
        );
    }
}
