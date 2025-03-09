package com.maximus.Airport_Gate_Management_System.gates;

import com.maximus.Airport_Gate_Management_System.flights.Flight;
import com.maximus.Airport_Gate_Management_System.flights.FlightRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
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
            Integer flightId,
            Integer gateId) {
        Gate gate = gateRepository.findById(gateId)
                .orElseThrow(() -> {
                    log.debug("Gate with ID {} not found. " +
                            "Throwing RuntimeException.", gateId);
                    return new RuntimeException("Gate not found");
                });
        if (gate.getFlight() != null) {
            log.trace("Gate ID: {} is occupied!",
                    gate.getId());
            return false;
        }
        if (!checkAvailabilityTime(gate)) {
            log.trace("Gate ID: {} is not available at the moment because of" +
                            "limited working time!",
                    gate.getId());
            return false;
        }
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> {
                    log.debug("Flight with ID: {} not found. " +
                            "Throwing RuntimeException.", flightId);
                    return new RuntimeException("Flight not found");
                });
        flight.setGate(gate);
        gate.setFlight(flight);
        flightRepository.save(flight);
        gateRepository.save(gate);
        log.trace("Flight number: {} was successfully assigned to the gate {}.",
                flight.getFlightNumber(),
                gate.getName());
        return true;
    }

    public List<Gate> getAvailableGates() {
        return gateRepository.findAllAvailableGates(LocalTime.now());
    }

    public List<Gate> getAvailableGates(LocalTime localTime) {
        return gateRepository.findAllAvailableGates(localTime);
    }

    public GateResponseDto saveGate(GateDto dto) {
        Gate gate = gateMapper.toGate(dto);
        Gate savedGate = gateRepository.save(gate);
        log.trace("Creating new gate with ID: {}.", gate.getId());

        return gateMapper.toGateResponseDto(savedGate);
    }

    public GateResponseDto updateGate(Integer id, GateDto dto) {
        Gate gate = gateRepository.findById(id).orElse(null);

        if (gate == null) {
            log.warn("Gate with ID: {} does not exist!", id);
            throw new EntityNotFoundException("Gate not found with ID: " + id);
        }

        gate = gateMapper.toGate(dto);
        gate.setId(id);

        Gate updatedGate = gateRepository.save(gate);
        log.debug("Updated gate with ID: {}.", updatedGate.getId());

        return gateMapper.toGateResponseDto(updatedGate);
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
                .orElseThrow(() ->
                        new EntityNotFoundException("Gate not found with ID: "
                                + id));
    }

    public void deleteById(Integer id) {
        log.trace("Deleting the gate with ID: {}.", id);
        gateRepository.deleteById(id);
    }

    private boolean checkAvailabilityTime(Gate gate) {
        var localTime = LocalTime.now();
        var openingTime = gate.getOpeningTime();
        var closingTime = gate.getClosingTime();
        if (openingTime.isBefore(localTime) && closingTime.isAfter(localTime))
            return true;
        if (openingTime.isAfter(closingTime)) {
            return openingTime.isBefore(localTime)
                    | closingTime.isAfter(localTime);
        }
        return false;
    }

    public void updateOpeningTime(Integer gateId, LocalTime localTime) {
        Gate gate = gateRepository.findById(gateId)
                .orElseThrow(() -> {
                    log.debug(
                            "Gate ID: {} for updating opening time is not found." +
                            "Throwing RuntimeException.",
                            gateId);
                    return new RuntimeException("Gate for updating not found");
                });
        gateRepository.updateGateOpeningTime(gate.getId(), localTime);
    }

    public void updateClosingTime(Integer gateId, LocalTime localTime) {
        Gate gate = gateRepository.findById(gateId)
                .orElseThrow(() -> {
                    log.debug(
                            "Gate ID: {} for updating closing time is not found." +
                                    "Throwing RuntimeException.",
                            gateId);
                    return new RuntimeException("Gate for updating not found");
                });
        gateRepository.updateGateClosingTime(gate.getId(), localTime);
    }

}