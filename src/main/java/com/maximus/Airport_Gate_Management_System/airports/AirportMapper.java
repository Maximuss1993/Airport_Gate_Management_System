package com.maximus.Airport_Gate_Management_System.airports;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AirportMapper {

    AirportMapper INSTANCE = Mappers.getMapper(AirportMapper.class);

    Airport toAirport(AirportDto dto);

    AirportResponseDto toAirportResponseDto(Airport airport);

//    @BeforeMapping
//    default void checkIfDtoIsNull(GateDto dto) {
//        if (dto == null) {
//            log.error("GateDto object is null. Cannot proceed with mapping.");
//            throw new IllegalArgumentException("DTO cannot be null");
//        }
//    }
//
//    @BeforeMapping
//    default void checkIfGateIsNull(Gate gate) {
//        if (gate == null) {
//            log.error("Gate object is null. Cannot proceed with mapping.");
//            throw new IllegalArgumentException("Gate object cannot be null");
//        }

//    }



//    public Airport toAirport(AirportDto dto) {

//        if (dto == null) {
//            log.error("The airport DTO is null. Throwing NullPointerException.");
//            throw new NullPointerException("The airport DTO should not be null!");
//        }
//
//        return  Airport.builder()
//                .name(dto.name())
//                .location(dto.location())
//                .build();
//    }
//
//    public AirportResponseDto toAirportResponseDto(Airport airport) {
//
//        if (airport == null) {
//            log.error("The airport is null. Throwing NullPointerException.");
//            throw new NullPointerException("The airport should not be null!");
//        }
//
//        return AirportResponseDto.builder()
//                .name(airport.getName())
//                .location(airport.getLocation())
//                .build();
//    }
}
