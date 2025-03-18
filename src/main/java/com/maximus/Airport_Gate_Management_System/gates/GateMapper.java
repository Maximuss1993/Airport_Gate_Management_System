package com.maximus.Airport_Gate_Management_System.gates;

import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mapper
public interface GateMapper {

    Logger log = LoggerFactory.getLogger(GateMapper.class);

    GateMapper INSTANCE = Mappers.getMapper(GateMapper.class);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "openingTime", target = "openingTime")
    @Mapping(source = "closingTime", target = "closingTime")
    Gate toGate(@Valid GateDto dto);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "openingTime", target = "openingTime")
    @Mapping(source = "closingTime", target = "closingTime")
    @Mapping(source = "flight", target = "flight")
    GateResponseDto toGateResponseDto(@Valid Gate gate);

    default void updateGateFromDto(GateDto dto, Gate gate) {
        dtoNullCheck(dto);
        gateNullCheck(gate);
        if (dto.name() != null)
            gate.setName(dto.name());
        if (dto.openingTime() != null)
            gate.setOpeningTime(dto.openingTime());
        if (dto.closingTime() != null)
            gate.setClosingTime(dto.closingTime());
    }

    private static void dtoNullCheck(GateDto dto) {
        if (dto == null) {
            log.error("The gate DTO is null. Throwing NullPointerException.");
            throw new NullPointerException("The gate DTO should not be null!");
        }
    }

    private static void gateNullCheck(Gate gate) {
        if (gate == null) {
            log.error("The gate is null. Throwing NullPointerException.");
            throw new NullPointerException("The gate should not be null!");
        }
    }

}
