package com.maximus.Airport_Gate_Management_System.gates;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GateMapper {

    public Gate toGate(GateDto dto) {
        dtoNullCheck(dto);
        return Gate.builder()
                .name(dto.name())
                .openingTime(dto.openingTime())
                .closingTime(dto.closingTime())
                .build();
    }

    public GateResponseDto toGateResponseDto(Gate gate) {
        gateNullCheck(gate);
        return GateResponseDto.builder()
                .name(gate.getName())
                .openingTime(gate.getOpeningTime())
                .closingTime(gate.getClosingTime())
                .build();
    }

    public void updateGateFromDto(GateDto dto, Gate gate) {
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
            throw new NullPointerException(
                    "The gate DTO should not be null!");
        }
    }

    private static void gateNullCheck(Gate gate) {
        if (gate == null) {
            log.error("The gate is null. Throwing NullPointerException.");
            throw new NullPointerException(
                    "The gate should not be null!");
        }
    }
}
