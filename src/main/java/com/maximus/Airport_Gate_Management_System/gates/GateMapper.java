package com.maximus.Airport_Gate_Management_System.gates;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GateMapper {

    GateMapper INSTANCE = Mappers.getMapper(GateMapper.class);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "openingTime", target = "openingTime")
    @Mapping(source = "closingTime", target = "closingTime")
    Gate toGate(GateDto dto);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "openingTime", target = "openingTime")
    @Mapping(source = "closingTime", target = "closingTime")
    @Mapping(source = "flight", target = "flight")
    GateResponseDto toGateResponseDto(Gate gate);

}
