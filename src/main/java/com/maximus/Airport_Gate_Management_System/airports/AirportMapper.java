package com.maximus.Airport_Gate_Management_System.airports;

import org.springframework.stereotype.Service;

@Service
public class AirportMapper {

    public Airport toAirport(AirportDto dto) {

        if(dto == null) {
            throw new NullPointerException(
                    "The airport DTO should not be null!");
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
