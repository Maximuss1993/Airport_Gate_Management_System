package com.maximus.Airport_Gate_Management_System.gates;

import com.maximus.Airport_Gate_Management_System.exceptions.ApiRequestException;
import com.maximus.Airport_Gate_Management_System.flights.Flight;
import com.maximus.Airport_Gate_Management_System.flights.FlightRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public boolean parkFlightOnGate(Integer flightId, Integer gateId) {
        Gate gate = getGate(gateId);
        checkGateAvailability(gate);
        parkFlightOnGateAndSave(flightId, gate);
        log.info("Flight with ID: {} successfully parked at Gate with ID: {}",
                flightId, gateId);
        return true;
    }

    @Transactional
    public boolean parkFlightOnFirstAvailableGate(Integer flightId) {
        var currentTime = LocalTime.now();
        Gate foundGate = getFirstAvailableGate(currentTime);
        parkFlightOnGateAndSave(flightId, foundGate);
        log.info("Flight with ID: {} successfully parked at the first available " +
                        "gate with ID: {}.", flightId, foundGate.getId());
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
        return gateMapper.toGateResponseDto(savedGate);
    }

    public GateResponseDto updateGate(Integer id, GateDto dto) {
        Gate gate = gateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Gate not found, ID: " + id));
        gateMapper.updateGateFromDto(dto, gate);
        Gate updatedGate = gateRepository.save(gate);
        log.debug("Gate with ID: {} has been successfully updated.",
                updatedGate.getId());
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
                .orElseThrow(() -> new EntityNotFoundException(
                        "Gate not found, ID: " + id));
    }

    public void deleteById(Integer id) {
        gateRepository.deleteById(id);
    }

    public Gate patchGate(Integer gateId, GateDto dto) {
        Gate gate = getGate(gateId);
        if (dto.name() != null)
            gate.setName(dto.name());
        if (dto.openingTime() != null)
            gate.setOpeningTime(dto.openingTime());
        if (dto.closingTime() != null)
            gate.setClosingTime(dto.closingTime());
        return gateRepository.save(gate);
    }

    private boolean checkAvailabilityTime(Gate gate) {
        var localTime = LocalTime.now();
        var openingTime = gate.getOpeningTime();
        var closingTime = gate.getClosingTime();
        if (openingTime.isBefore(closingTime))
            return !localTime.isBefore(openingTime)
                    && !localTime.isAfter(closingTime);
        return !localTime.isBefore(openingTime)
                || !localTime.isAfter(closingTime);
    }

    private void checkGateAvailability(Gate gate) {
        if (gate.getFlight() != null) {
            String message = "Gate with ID: " + gate.getId()
                    + " is already occupied!";
            log.warn(message);
            throw new ApiRequestException(message);
        }
        if (!checkAvailabilityTime(gate)) {
            String message = "Gate with ID: " + gate.getId()
                    + " is currently unavailable!";
            log.warn(message);
            throw new ApiRequestException(message);
        }
    }

    private Gate getFirstAvailableGate(LocalTime currentTime) {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Gate> gatePage = gateRepository
                .findFirstAvailableGate(currentTime, pageable);
        if (!gatePage.hasContent())
            throw new EntityNotFoundException("No available gates found at " +
                    "the current time: " + currentTime);
        return gatePage.getContent().get(0);
    }

    private Flight getFlight(Integer flightId) {
        return flightRepository.findById(flightId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Flight not found, ID: " + flightId));
    }

    private Gate getGate(Integer gateId) {
        return gateRepository.findById(gateId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Gate not found, ID:" + gateId));
    }

    private void parkFlightOnGateAndSave(Integer flightId, Gate gate) {
        Flight flight = getFlight(flightId);
        flight.setGate(gate);
        gate.setFlight(flight);
        flightRepository.save(flight);
        gateRepository.save(gate);
        log.trace("Flight number: {} successfully assigned to gate: {}.",
                flight.getFlightNumber(), gate.getName());
    }
}