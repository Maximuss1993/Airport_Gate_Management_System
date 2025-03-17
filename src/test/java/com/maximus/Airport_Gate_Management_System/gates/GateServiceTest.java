package com.maximus.Airport_Gate_Management_System.gates;

import com.maximus.Airport_Gate_Management_System.flights.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GateServiceTest {

    @InjectMocks
    private GateService gateService;

    @Mock
    private GateRepository gateRepository;

    @Mock
    private GateMapper gateMapper;

    @Mock
    private FlightRepository flightRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_successfully_save_a_gate() {

        GateDto dto = new GateDto(
                "TestGate",
                LocalTime.of(1,0),
                LocalTime.of(2, 0));

        Gate gate = Gate.builder()
                .name("TestGate")
                .openingTime(LocalTime.of(1,0))
                .closingTime(LocalTime.of(2,0))
                .build();

        Gate savedGate = Gate.builder()
                .name("TestGate")
                .openingTime(LocalTime.of(1,0))
                .closingTime(LocalTime.of(2,0))
                .build();

        when(gateMapper.toGate(dto)).thenReturn(gate);
        when(gateRepository.save(gate)).thenReturn(savedGate);
        when(gateMapper.toGateResponseDto(savedGate))
                .thenReturn(new GateResponseDto(
                        "TestGate",
                        LocalTime.of(1,0),
                        LocalTime.of(2,0)));

        GateResponseDto responseDto = gateService.saveGate(dto);

        assertEquals(dto.name(), responseDto.name());
        assertEquals(dto.openingTime(), responseDto.openingTime());
        assertEquals(dto.closingTime(), responseDto.closingTime());

        verify(gateMapper, times(1)).toGate(dto);
        verify(gateRepository, times(1)).save(gate);
        verify(gateMapper, times(1))
                .toGateResponseDto(savedGate);
    }

    @Test
    public void should_return_all_gates() {

        Gate gate1 = Gate.builder()
                .name("TestGate1")
                .build();

        Gate gate2 = Gate.builder()
                .name("TestGate2")
                .build();

        List<Gate> gates = new ArrayList<>();
        gates.add(gate1);
        gates.add(gate2);

        when(gateRepository.findAll()).thenReturn(gates);
        when(gateMapper.toGateResponseDto(any(Gate.class)))
                .thenReturn(new GateResponseDto(
                        "TestGate",
                        LocalTime.of(1,0),
                        LocalTime.of(2,0))
                );

        List<GateResponseDto> responseDtos = gateService.findAllGates();

        assertEquals(gates.size(), responseDtos.size());
        verify(gateRepository, times(1)).findAll();
    }

    @Test
    public void should_return_gate_by_id() {

        Integer gateId = 1;

        Gate gate = Gate.builder()
                .name("TestGate")
                .openingTime(LocalTime.of(1,0))
                .closingTime(LocalTime.of(2,0))
                .build();

        when(gateMapper.toGateResponseDto(any(Gate.class)))
                .thenReturn(new GateResponseDto(
                        "TestGate",
                        LocalTime.of(1,0),
                        LocalTime.of(2,0))
                );
        when(gateRepository.findById(gateId)).thenReturn(Optional.of(gate));

        GateResponseDto responseDto = gateService.findById(gateId);

        assertEquals(gate.getName(), responseDto.name());
        assertEquals(gate.getOpeningTime(), responseDto.openingTime());
        assertEquals(gate.getClosingTime(), responseDto.closingTime());

        verify(gateRepository, times(1))
                .findById(gateId);

    }

}