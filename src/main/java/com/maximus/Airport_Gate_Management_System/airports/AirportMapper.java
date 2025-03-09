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
        return  Airport.builder()
                .name(dto.name())
                .location(dto.location())
                .build();
    }

    public AirportResponseDto toAirportResponseDto(Airport airport) {
        return AirportResponseDto.builder()
                .name(airport.getName())
                .location(airport.getLocation())
                .build();
    }
}
