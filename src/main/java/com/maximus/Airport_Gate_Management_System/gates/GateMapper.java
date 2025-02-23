package com.maximus.Airport_Gate_Management_System.gates;

import org.springframework.stereotype.Service;

@Service
public class GateMapper {

    public Gate toGate(GateDto dto) {

        if(dto == null) {
            throw new NullPointerException(
                    "The gate DTO should not be null!");
        }

        var gate = new Gate();
        gate.setAvailable(dto.available());

        return gate;
    }

    public GateResponseDto toGateResponseDto(Gate gate) {
        return new GateResponseDto(
                gate.isAvailable()
        );
    }
}
