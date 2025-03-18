package com.maximus.Airport_Gate_Management_System.gates;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class GateMapperTest {

    private GateMapper gateMapper;

    @BeforeEach
    void setUp() {
        gateMapper = GateMapper.INSTANCE;
    }

    @Test
    public void should_map_gate_dto_to_gate() {
         GateDto dto = new GateDto(
                 "TestGateDto",
                 LocalTime.of(1,0),
                 LocalTime.of(2, 0));

         Gate gate = gateMapper.toGate(dto);
         assertEquals(dto.name(), gate.getName());
         assertEquals(dto.openingTime(), gate.getOpeningTime());
         assertEquals(dto.closingTime(), gate.getClosingTime());
    }

    @Test
    public void should_throw_null_pointer_exception_when_dto_is_null() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () ->  gateMapper.toGate(null),
                "Should throw NullPointerException."
        );
        assertEquals("The gate DTO should not be null!",
                exception.getMessage());
    }

    @Test
    public void should_map_gate_to_gate_response_dto() {
        Gate gate = Gate.builder()
                .name("TestGate")
                .openingTime(LocalTime.of(1,0))
                .closingTime(LocalTime.of(3,0))
                .build();

        GateResponseDto responseDto = gateMapper.toGateResponseDto(gate);
        assertEquals(gate.getName(), responseDto.name());
        assertEquals(gate.getOpeningTime(), responseDto.openingTime());
        assertEquals(gate.getClosingTime(), responseDto.closingTime());
    }

    @Test
    public void should_throw_null_pointer_exception_when_gate_is_null() {
        GateDto gateDto = null;
        Gate gate = new Gate();
        assertThrows(NullPointerException.class,
                () -> gateMapper.updateGateFromDto(gateDto, gate));
    }

    @Test
    public void should_update_gate_from_dto() {
        var oldOpeningTime = LocalTime.of(1,0);
        var oldClosingTime = LocalTime.of(2, 0);
        var newOpeningTime = LocalTime.of(12,0);
        var newClosingTime = LocalTime.of(13, 0);

        Gate gate = Gate.builder()
                .name("TestGate")
                .openingTime(oldOpeningTime)
                .closingTime(oldClosingTime)
                .build();

        GateDto dto = new GateDto(
                "TestGateDto",
                newOpeningTime,
                newClosingTime);

        gate = gateMapper.toGate(dto);
        assertEquals(gate.getName(), dto.name());
        assertEquals(gate.getOpeningTime(), dto.openingTime());
        assertEquals(gate.getClosingTime(), dto.closingTime());
    }

}