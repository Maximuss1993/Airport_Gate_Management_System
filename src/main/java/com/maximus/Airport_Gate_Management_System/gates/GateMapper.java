package com.maximus.Airport_Gate_Management_System.gates;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface GateMapper {

    GateMapper INSTANCE = Mappers.getMapper(GateMapper.class);

    Gate toGate(GateDto dto);

    GateResponseDto toGateResponseDto(@Valid Gate gate);

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

    default void updateGateFromDto(@NotNull GateDto dto, @NotNull Gate gate) {
//        dtoNullCheck(dto);
//        gateNullCheck(gate);
        if (dto.name() != null)
            gate.setName(dto.name());
        if (dto.openingTime() != null)
            gate.setOpeningTime(dto.openingTime());
        if (dto.closingTime() != null)
            gate.setClosingTime(dto.closingTime());
    }

//    private static void dtoNullCheck(GateDto dto) {
//        if (dto == null) {
//            log.error("The gate DTO is null. Throwing NullPointerException.");
//            throw new NullPointerException("The gate DTO should not be null!");
//        }
//    }
//
//    private static void gateNullCheck(Gate gate) {
//        if (gate == null) {
//            log.error("The gate is null. Throwing NullPointerException.");
//            throw new NullPointerException("The gate should not be null!");
//        }
//    }
}
//    public Gate toGate(GateDto dto) {
//        if (dto == null) {
//            log.error("The gate DTO is null. Throwing NullPointerException.");
//            throw new NullPointerException("The gate DTO should not be null!");
//        }
//        return  Gate.builder()
//                .name(dto.name())
//                .openingTime(dto.openingTime())
//                .closingTime(dto.closingTime())
//                .build();
//    }
//
//    public GateResponseDto toGateResponseDto(Gate gate) {
//        if (gate == null) {
//            log.error("The gate is null. Throwing NullPointerException.");
//            throw new NullPointerException("The gate should not be null!");
//        }
//        return GateResponseDto.builder()
//                .name(gate.getName())
//                .openingTime(gate.getOpeningTime())
//                .closingTime(gate.getClosingTime())
//                .build();
//    }

