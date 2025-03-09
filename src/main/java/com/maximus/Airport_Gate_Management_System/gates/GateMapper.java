package com.maximus.Airport_Gate_Management_System.gates;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GateMapper {

    public Gate toGate(GateDto dto) {

        if (dto == null) {
            log.error("The gate DTO is null. " +
                    "Throwing NullPointerException.");
            throw new NullPointerException(
                    "The gate DTO should not be null!");
        }
        return Gate.builder()
                .name(dto.name())
                .openingTime(dto.openingTime())
                .closingTime(dto.closingTime())
                .build();
    }

    public GateResponseDto toGateResponseDto(Gate gate) {
        return GateResponseDto.builder()
                .name(gate.getName())
                .openingTime(gate.getOpeningTime())
                .closingTime(gate.getClosingTime())
                .build();
    }
}
