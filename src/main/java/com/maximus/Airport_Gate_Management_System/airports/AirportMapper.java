package com.maximus.Airport_Gate_Management_System.airports;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AirportMapper {

    AirportMapper INSTANCE = Mappers.getMapper(AirportMapper.class);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "location", target = "location")
    Airport toAirport(AirportDto dto);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "location", target = "location")
    AirportResponseDto toAirportResponseDto(Airport airport);

}
