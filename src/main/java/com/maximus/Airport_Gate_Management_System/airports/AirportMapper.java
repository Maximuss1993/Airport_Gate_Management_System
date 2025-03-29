package com.maximus.Airport_Gate_Management_System.airports;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AirportMapper {

  AirportMapper INSTANCE = Mappers.getMapper(AirportMapper.class);

  Airport toAirport(AirportDto dto);

  AirportResponseDto toAirportResponseDto(Airport airport);

}
