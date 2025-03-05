package com.maximus.Airport_Gate_Management_System.gates;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GateMapper {

    public Gate toGate(GateDto dto) {

        if (dto == null) {
            log.error("The gate DTO is null. Throwing NullPointerException.");
            throw new NullPointerException("The gate DTO should not be null!");
        }
        var gate = new Gate();
        gate.setName(dto.name());

        return gate;
    }

    public GateResponseDto toGateResponseDto(Gate gate) {
        return new GateResponseDto(
                gate.getName()
        );
    }
}
