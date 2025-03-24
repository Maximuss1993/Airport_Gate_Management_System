package com.maximus.Airport_Gate_Management_System.flights;

import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;


@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface FlightMapper {

    FlightMapper INSTANCE = Mappers.getMapper(FlightMapper.class);

    Flight toFlight(FlightDto dto);

    FlightResponseDto toFlightResponseDto(Flight airport);


//    public Flight toFlight(FlightDto dto) {
//
//        if (dto == null) {
//            log.error("The flight DTO is null. Throwing NullPointerException.");
//            throw new NullPointerException("The flight DTO should not be null!");
//        }
//
//        return  Flight.builder()
//                .flightNumber(dto.flightNumber())
//                .arrivingTime(dto.arrivingTime())
//                .build();
//    }
//
//    public FlightResponseDto toFlightResponseDto(Flight flight) {
//
//        if (flight == null) {
//            log.error("The flight  is null. Throwing NullPointerException.");
//            throw new NullPointerException("The flight should not be null!");
//        }
//
//        return FlightResponseDto.builder()
//                .flightNumber(flight.getFlightNumber())
//                .arrivingTime(flight.getArrivingTime())
//                .build();
//    }
}
