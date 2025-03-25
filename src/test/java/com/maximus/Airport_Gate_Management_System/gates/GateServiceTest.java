package com.maximus.Airport_Gate_Management_System.gates;

import com.maximus.Airport_Gate_Management_System.exceptions.GateOccupiedException;
import com.maximus.Airport_Gate_Management_System.exceptions.GateUnavailableTimeException;
import com.maximus.Airport_Gate_Management_System.flights.Flight;
import com.maximus.Airport_Gate_Management_System.flights.FlightRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class GateServiceTest {

    @Spy
    @InjectMocks
    private GateService gateService;

    @Mock
    private GateRepository gateRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private GateMapper gateMapper;

    @Mock
    private Logger log;

    private Gate testGate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_successfully_save_gate() {
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

    @Test
    public void should_successfully_delete_gate_by_id() {
        Integer id = 1;
        gateService.deleteById(id);
        verify(gateRepository, times(1))
                .deleteById(id);
    }

    @Test
    public void should_successfully_update_gate_from_dto() {
        Integer gateId = 1;

        Gate gate = Gate.builder()
                .name("TestGate")
                .id(gateId)
                .openingTime(LocalTime.of(1, 0))
                .closingTime(LocalTime.of(2, 0))
                .build();

        GateDto dto = new GateDto(
                "UpdatedTestGate",
                LocalTime.of(3, 0),
                LocalTime.of(4, 0));

        Gate updatedGate = Gate.builder()
                .id(gateId)
                .name("UpdatedTestGate")
                .openingTime(LocalTime.of(3, 0))
                .closingTime(LocalTime.of(4, 0))
                .build();

        GateResponseDto responseDto = GateResponseDto.builder()
                .name("UpdatedTestGate")
                .openingTime(LocalTime.of(3, 0))
                .closingTime(LocalTime.of(4, 0))
                .build();

        when(gateRepository.findById(gateId)).thenReturn(Optional.of(gate));
        when(gateMapper.toGateResponseDto(updatedGate)).thenReturn(responseDto);

        GateResponseDto result = gateService.updateGate(gateId, dto);

        verify(gateRepository, times(1)).save(updatedGate);
        assertEquals("UpdatedTestGate", result.name());
        assertEquals(LocalTime.of(3, 0), result.openingTime());
        assertEquals(LocalTime.of(4, 0), result.closingTime());
    }

    @Test
    public void should_throw_exception_when_dto_is_null() {
        Integer id = 1;
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class, () -> {
                    gateService.patchGate(id, null);
                });
        assertEquals("GateDto cannot be null", thrown.getMessage());
    }

    @Test
    public void should_successfully_patch_gate_when_dto_is_valid() {
        Integer id = 1;
        Gate gate = Gate.builder()
                .id(id)
                .name("TestGate")
                .openingTime(LocalTime.of(1, 0))
                .closingTime(LocalTime.of(2, 0))
                .build();
        GateDto dto = new GateDto(
                "UpdatedTestGate",
                LocalTime.of(3, 0),
                LocalTime.of(4, 0));

        when(gateRepository.findById(id)).thenReturn(Optional.of(gate));
        when(gateRepository.save(gate)).thenReturn(gate);

        Gate result = gateService.patchGate(id, dto);

        assertEquals("UpdatedTestGate", result.getName());
        assertEquals(LocalTime.of(3, 0), result.getOpeningTime());
        assertEquals(LocalTime.of(4, 0), result.getClosingTime());
        verify(gateRepository, times(1)).save(gate);
    }

    @Test
    public void should_not_update_gate_when_dto_has_null_values() {
        Integer id = 1;
        Gate gate = Gate.builder()
                .id(id)
                .name("TestGate")
                .openingTime(LocalTime.of(1, 0))
                .closingTime(LocalTime.of(2, 0))
                .build();

        GateDto dto = new GateDto(null, null, null);

        when(gateRepository.findById(id)).thenReturn(Optional.of(gate));
        when(gateRepository.save(gate)).thenReturn(gate);

        Gate result = gateService.patchGate(id, dto);

        assertEquals("TestGate", result.getName());
        assertEquals(LocalTime.of(1, 0), result.getOpeningTime());
        assertEquals(LocalTime.of(2, 0), result.getClosingTime());
        verify(gateRepository, times(1)).save(gate);
    }

    @Test
    public void should_successfully_park_flight_on_gate() {
        Integer flightId = 1001;
        Integer gateId = 1;

        Gate gate = Gate.builder()
                .id(gateId)
                .name("Gate 1")
                .openingTime(LocalTime.of(5, 0))
                .closingTime(LocalTime.of(23, 0))
                .build();

        when(gateRepository.findById(gateId)).thenReturn(Optional.of(gate));
        doNothing().when(gateService).checkGateAvailabilityTimeAndOccupation(gate);
        doNothing().when(gateService).parkFlightOnGateAndSave(flightId, gate);

        boolean result = gateService.parkFlightOnGate(flightId, gateId);

        assertTrue(result);
        verify(gateService, times(1))
                .checkGateAvailabilityTimeAndOccupation(gate);
        verify(gateService, times(1))
                .parkFlightOnGateAndSave(flightId, gate);
    }

    @Test
    public void should_throw_exception_when_gate_not_found() {
        Integer flightId = 1001;
        Integer gateId = 1;
        when(gateRepository.findById(gateId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            gateService.parkFlightOnGate(flightId, gateId);
        });
    }

    @Test
    public void should_throw_exception_when_gate_is_not_available() {
        Integer gateId1 = 1;
        Integer gateId2 = 2;
        Integer flightId = 1;

        Gate gate1 = Gate.builder()
                .id(gateId1)
                .name("G-1")
                .openingTime(LocalTime.of(1, 0))
                .closingTime(LocalTime.of(3, 0))
                .build();
        Gate gate2 = Gate.builder()
                .id(gateId2)
                .name("G-2")
                .openingTime(LocalTime.of(6, 0))
                .closingTime(LocalTime.of(4, 0))
                .build();
        Flight flight = Flight.builder()
                .id(flightId)
                .flightNumber("F-123")
                .arrivingTime(LocalTime.of(5, 0))
                .build();

        when(gateRepository.findById(gateId1)).thenReturn(Optional.of(gate1));
        when(gateRepository.findById(gateId2)).thenReturn(Optional.of(gate2));
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));

        doThrow(new GateUnavailableTimeException("Gate is unavailable"))
                .when(gateService).checkGateAvailabilityTimeAndOccupation(gate1);
        doThrow(new GateUnavailableTimeException("Gate is unavailable"))
                .when(gateService).checkGateAvailabilityTimeAndOccupation(gate2);

        assertThrows(GateUnavailableTimeException.class,
                () -> gateService.parkFlightOnGate(flightId, gateId1));
        assertThrows(GateUnavailableTimeException.class,
                () -> gateService.parkFlightOnGate(flightId, gateId2));
    }

    @Test
    void should_park_flight_on_first_available_gate() {
        Integer flightId = 1;
        Gate gate = Gate.builder()
                .id(1)
                .name("G-1")
                .openingTime(LocalTime.of(6, 0))
                .closingTime(LocalTime.of(18, 0))
                .flight(null)
                .build();
        Flight flight = Flight.builder()
                .id(flightId)
                .flightNumber("F-123")
                .arrivingTime(LocalTime.of(8, 0))
                .build();
        when(gateRepository.findFirstAvailableGate(any(LocalTime.class),
                any(Pageable.class))).thenReturn(new PageImpl<>(List.of(gate)));
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        boolean result = gateService.parkFlightOnFirstAvailableGate(flightId);
        assertTrue(result);
        verify(gateRepository).save(gate);
        verify(flightRepository).save(flight);
    }

    @Test
    void should_throw_exception_when_flight_not_found() {
        Integer flightId = 1;
        Gate gate = Gate.builder()
                .id(1)
                .name("G-1")
                .openingTime(LocalTime.of(6, 0))
                .closingTime(LocalTime.of(18, 0))
                .flight(null) // Gate is free
                .build();
        when(gateRepository.findFirstAvailableGate(any(LocalTime.class),
                any(Pageable.class))).thenReturn(new PageImpl<>(List.of(gate)));
        when(flightRepository.findById(flightId)).thenReturn(
                Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                gateService.parkFlightOnFirstAvailableGate(flightId));
        assertEquals("Flight not found, ID: " + flightId,
                exception.getMessage());
    }

    @Test
    void test_park_out_flight_from_gate_success() {
        Integer gateId = 1;
        Gate gate = new Gate.GateBuilder()
                .id(gateId)
                .flight(new Flight())
                .build();
        gateRepository.save(gate);
        boolean result = gateService.parkOutFlightFromGate(gateId);
        assertTrue(result);
    }

    @Test
    void test_park_out_flight_from_gate_failure() {
        Integer gateId = 1;
        Gate gate = new Gate.GateBuilder()
                .id(gateId)
                .build();
        gateRepository.save(gate);
        doThrow(new RuntimeException("Database error")).when(gateRepository)
                .parkOutFlightFromGate(gateId);
        boolean result = gateService.parkOutFlightFromGate(gateId);
        assertFalse(result);
    }

    @Test
    void test_is_gate_free_when_gate_has_flight() {
        Integer gateId = 1;
        Gate gate = Gate.builder()
                .id(gateId)
                .flight(new Flight())
                .build();
        when(gateRepository.findById(gateId)).thenReturn(Optional.of(gate));
        boolean result = gateService.isGateFree(gate.getId());
        assertFalse(result);
    }

    @Test
    void test_is_gate_free_when_gate_is_free() {
        Integer gateId = 1;
        Gate gate = Gate.builder()
                .id(gateId)
                .flight(null)
                .build();
        when(gateRepository.findById(gateId)).thenReturn(Optional.of(gate));
        boolean result = gateService.isGateFree(gate.getId());
        assertTrue(result);
    }

    @Test
    void test_check_availability_time_within_time_range() {
        Integer gateId = 1;
        Gate gate = Gate.builder()
                .id(gateId)
                .openingTime(LocalTime.of(0, 0))
                .closingTime(LocalTime.of(23, 59))
                .flight(null)
                .build();
        boolean result = gateService.checkAvailabilityTime(gate);
        assertTrue(result);
    }

    @Test
    void test_check_availability_time_out_of_time_range() {
        Gate gate = Gate.builder()
                .openingTime(LocalTime.of(0, 0))
                .closingTime(LocalTime.of(0, 1))
                .flight(null)
                .build();
        boolean result = gateService.checkAvailabilityTime(gate);
        assertFalse(result);
    }

    @Test
    void test_check_gate_availability_time_and_occupation_gate_occupied() {
        var gateId = 1;
        Gate gate = Gate.builder()
                .id(gateId)
                .flight(new Flight())
                .build();
        GateOccupiedException thrown = assertThrows(GateOccupiedException.class,
                () -> {gateService.checkGateAvailabilityTimeAndOccupation(gate);
        });
        assertEquals("Gate with ID: " + gateId + " is already occupied!",
                thrown.getMessage());
    }

    @Test
    void test_check_gate_availability_time_and_occupation_gate_unavailable() {
        var gateId = 1;
        Gate gate = Gate.builder()
                .id(gateId)
                .openingTime(LocalTime.of(0, 0))
                .closingTime(LocalTime.of(0, 1))
                .build();
        GateUnavailableTimeException thrown = assertThrows(
                GateUnavailableTimeException.class, () -> {
            gateService.checkGateAvailabilityTimeAndOccupation(gate);
        });
    }
}