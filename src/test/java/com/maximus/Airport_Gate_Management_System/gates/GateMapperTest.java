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

}