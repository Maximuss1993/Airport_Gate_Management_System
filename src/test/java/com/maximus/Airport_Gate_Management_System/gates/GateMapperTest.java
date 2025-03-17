package com.maximus.Airport_Gate_Management_System.gates;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class GateMapperTest {

    private GateMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new GateMapper();
    }

    @Test
    public void shouldMapGateDtoToGate() {
         GateDto dto = new GateDto(
                 "TestGateDto",
                 LocalTime.of(1,0),
                 LocalTime.of(2, 0));

         Gate gate = mapper.toGate(dto);
         assertEquals(dto.name(), gate.getName());
         assertEquals(dto.openingTime(), gate.getOpeningTime());
         assertEquals(dto.closingTime(), gate.getClosingTime());
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenDtoIsNull() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () ->  mapper.toGate(null),
                "Should throw NullPointerException."
        );
        assertEquals("The gate DTO should not be null!",
                exception.getMessage());
    }

    @Test
    public void shouldMapGateToGateResponseDto() {
        Gate gate = Gate.builder()
                .name("TestGate")
                .openingTime(LocalTime.of(1,0))
                .closingTime(LocalTime.of(3,0))
                .build();

        GateResponseDto responseDto = mapper.toGateResponseDto(gate);
        assertEquals(gate.getName(), responseDto.name());
        assertEquals(gate.getOpeningTime(), responseDto.openingTime());
        assertEquals(gate.getClosingTime(), responseDto.closingTime());
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenGateIsNull() {
        var exception = assertThrows(NullPointerException.class,
                () ->  mapper.toGateResponseDto(null),
                "Should throw NullPointerException."
        );
        assertEquals("The gate should not be null!",
                exception.getMessage());
    }

    @Test
    public void shouldUpdateGateFromDto() {
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

        mapper.updateGateFromDto(dto, gate);
        assertEquals(gate.getName(), dto.name());
        assertEquals(gate.getOpeningTime(), dto.openingTime());
        assertEquals(gate.getClosingTime(), dto.closingTime());
    }

    @Test
    public void should_trow_exception_when_updating_from_null_dto() {
        var oldOpeningTime = LocalTime.of(1,0);
        var oldClosingTime = LocalTime.of(2, 0);

        Gate gate = Gate.builder()
                .name("TestGate")
                .openingTime(oldOpeningTime)
                .closingTime(oldClosingTime)
                .build();

        var exception = assertThrows(NullPointerException.class,
                () ->  mapper.updateGateFromDto(null, gate),
                "Should throw NullPointerException."
        );
        assertEquals("The gate DTO should not be null!",
                exception.getMessage());
    }

    @Test
    public void should_trow_exception_when_updating_gate_is_null() {
        var newOpeningTime = LocalTime.of(12,0);
        var newClosingTime = LocalTime.of(13, 0);

        GateDto dto = new GateDto(
                "TestGateDto",
                newOpeningTime,
                newClosingTime);

        var exception = assertThrows(NullPointerException.class,
                () ->  mapper.updateGateFromDto(dto, null),
                "Should throw NullPointerException."
        );
        assertEquals("The gate should not be null!",
                exception.getMessage());
    }

}