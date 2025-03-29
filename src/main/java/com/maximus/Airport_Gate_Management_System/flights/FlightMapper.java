package com.maximus.Airport_Gate_Management_System.flights;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;


@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface FlightMapper {

  FlightMapper INSTANCE = Mappers.getMapper(FlightMapper.class);

  Flight toFlight(FlightDto dto);

  FlightResponseDto toFlightResponseDto(Flight airport);

}
