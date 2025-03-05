package com.maximus.Airport_Gate_Management_System.gates;

import com.maximus.Airport_Gate_Management_System.flights.Flight;
import com.maximus.Airport_Gate_Management_System.flights.FlightRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GateService {

    private final GateRepository gateRepository;
    private final GateMapper gateMapper;
    private final FlightRepository flightRepository;

    public GateService(GateRepository gateRepository,
                       GateMapper gateMapper,
                       FlightRepository flightRepository) {
        this.gateRepository = gateRepository;
        this.gateMapper = gateMapper;
        this.flightRepository = flightRepository;
    }

    @Transactional
    public boolean parkFlightOnGate(
            Integer flightId, Integer gateId) {

        Gate gate = gateRepository.findById(gateId)
                .orElseThrow(() -> new RuntimeException("Gate not found"));

        if (gate.getFlight() != null) {
            log.debug("Gate ID: {} is not available!",
                    gate.getId());
            return false;
        }

        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> {
                    log.error("Flight with ID {} not found. " +
                            "Throwing RuntimeException.", flightId);
                    return new RuntimeException("Flight not found");
                });

        flight.setGate(gate);
        gate.setFlight(flight);

        flightRepository.save(flight);
        gateRepository.save(gate);

        log.debug("Flight number: {} was successfully assigned to gate {}.",
                flight.getFlightNumber(),
                gate.getName());

        return true;
    }

    public List<Gate> getAvailableGates() {
        return gateRepository.findByFlightIsNull();
    }

    public GateResponseDto saveGate(GateDto dto) {
        Gate gate = gateMapper.toGate(dto);
        Gate savedGate = gateRepository.save(gate);
        log.trace("Creating new gate with ID: {}.", gate.getId());

        return gateMapper.toGateResponseDto(savedGate);
    }

    public List<GateResponseDto> findAllGates() {
        return gateRepository
                .findAll()
                .stream()
                .map(gateMapper::toGateResponseDto)
                .collect(Collectors.toList());
    }

    public GateResponseDto findById(Integer id) {
        return gateRepository.findById(id)
                .map(gateMapper::toGateResponseDto)
                .orElse(null);
    }

    public void deleteById(Integer id) {
        log.trace("Deleting the gate with ID: {}.", id);
        gateRepository.deleteById(id);
    }

}