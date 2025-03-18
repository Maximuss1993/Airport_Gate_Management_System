package com.maximus.Airport_Gate_Management_System.flights;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FlightMapper {

    FlightMapper INSTANCE = Mappers.getMapper(FlightMapper.class);

    @Mapping(source = "flightNumber", target = "flightNumber")
    @Mapping(source = "arrivingTime", target = "arrivingTime")
    Flight toFlight(FlightDto dto);

    @Mapping(source = "flightNumber", target = "flightNumber")
    @Mapping(source = "arrivingTime", target = "arrivingTime")
    FlightResponseDto toFlightResponseDto(Flight airport);
}