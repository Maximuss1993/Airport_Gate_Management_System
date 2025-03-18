package com.maximus.Airport_Gate_Management_System.gates;

import com.maximus.Airport_Gate_Management_System.flights.FlightRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GateServiceTest {

    @InjectMocks
    private GateService gateService;

    @Mock
    private GateRepository gateRepository;

    @Mock
    private GateMapper gateMapper = GateMapper.INSTANCE;

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
                .thenReturn(GateResponseDto.builder()
                        .name("TestGate")
                        .openingTime(LocalTime.of(1,0))
                        .closingTime(LocalTime.of(2,0))
                        .build());

        GateResponseDto responseDto = gateService.saveGate(dto);

        assertEquals(dto.name(), responseDto.name());
        assertEquals(dto.openingTime(), responseDto.openingTime());
        assertEquals(dto.closingTime(), responseDto.closingTime());

        verify(gateRepository, times(1)).save(gate);
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
                .thenAnswer(invocation -> {
                    Gate gate = invocation.getArgument(0);
                    return GateResponseDto.builder()
                            .name(gate.getName())
                            .openingTime(gate.getOpeningTime())
                            .closingTime(gate.getClosingTime())
                            .build();
                });

        List<GateResponseDto> responseDtos = gateService.findAllGates();

        assertEquals(gates.size(), responseDtos.size());
        verify(gateRepository, times(1)).findAll();
    }

    @Test
    public void should_return_gate_by_id() {

        Integer gateId = 1;

        Gate gate = Gate.builder()
                .name("TestGate")
                .openingTime(LocalTime.of(1, 0))
                .closingTime(LocalTime.of(2, 0))
                .build();

        when(gateMapper.toGateResponseDto(any(Gate.class)))
                .thenReturn(GateResponseDto.builder()
                        .name("TestGate")
                        .openingTime(LocalTime.of(1,0))
                        .closingTime(LocalTime.of(2,0))
                        .build());

        when(gateRepository.findById(gateId)).thenReturn(Optional.of(gate));

        Optional<GateResponseDto> responseDto = gateService.findById(gateId);

        assertTrue(responseDto.isPresent(),
                "Gate response should be present");
        assertEquals(gate.getName(), responseDto.get().name());
        assertEquals(gate.getOpeningTime(), responseDto.get()
                .openingTime());
        assertEquals(gate.getClosingTime(), responseDto.get()
                .closingTime());
        verify(gateRepository, times(1))
                .findById(gateId);
    }

    @Test
    public void should_throw_exception_if_gate_not_found() {
        Integer gateId = 1;

        when(gateRepository.findById(gateId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> gateService.findById(gateId));
    }

    @Test
    public void should_return_all_available_gates_in_target_time() {
        LocalTime testOpeningTime = LocalTime.of(1, 0);
        LocalTime testClosingTime = LocalTime.of(3, 0);

        Gate gate1 = Gate.builder()
                .name("TestGate1")
                .openingTime(testOpeningTime)
                .closingTime(testClosingTime)
                .build();

        Gate gate2 = Gate.builder()
                .name("TestGate2")
                .openingTime(testOpeningTime)
                .closingTime(testClosingTime)
                .build();

        List<Gate> gates = Arrays.asList(gate1, gate2);

        var targetTime = LocalTime.of(2, 0);

        when(gateRepository.findAllAvailableGates(targetTime))
                .thenReturn(gates);

        when(gateMapper.toGateResponseDto(any(Gate.class)))
                .thenAnswer(invocation -> {
                    Gate gate = invocation.getArgument(0);
                    return GateResponseDto.builder()
                            .name(gate.getName())
                            .openingTime(gate.getOpeningTime())
                            .closingTime(gate.getClosingTime())
                            .build();
                });

        List<GateResponseDto> responseDtos = gateService
                .getAvailableGates(targetTime);

        assertEquals(gates.size(), responseDtos.size(),
                "The number of available gates should match.");

        assertEquals("TestGate1", responseDtos.get(0)
                .name());
        assertEquals(testOpeningTime,
                responseDtos.get(0).openingTime());
        assertEquals(testClosingTime,
                responseDtos.get(0).closingTime());

        assertEquals("TestGate2", responseDtos.get(1)
                .name());
        assertEquals(testOpeningTime,
                responseDtos.get(1).openingTime());
        assertEquals(testClosingTime,
                responseDtos.get(1).closingTime());

        verify(gateRepository, times(1))
                .findAllAvailableGates(targetTime);
    }

}