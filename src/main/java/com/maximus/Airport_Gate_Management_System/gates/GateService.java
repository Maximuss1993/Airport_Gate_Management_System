package com.maximus.Airport_Gate_Management_System.gates;

import com.maximus.Airport_Gate_Management_System.customExceptions.FlightNotFoundException;
import com.maximus.Airport_Gate_Management_System.customExceptions.GateNotFoundException;
import com.maximus.Airport_Gate_Management_System.flights.Flight;
import com.maximus.Airport_Gate_Management_System.flights.FlightRepository;
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
        if (gate.getFlight() != null) {
            log.debug("Gate ID: {} is occupied!", gateId);
            return false;
        }
        if (!checkAvailabilityTime(gate)) {
            log.debug("Gate ID: {} currently is not available.", gateId);
            return false;
        }
        parkFlightOnGateAndSave(flightId, gate);
        return true;
    }

    @Transactional
    public boolean parkFlightOnFirstAvailableGate(Integer flightId) {
        var currentTime = LocalTime.now();
        Pageable pageable = PageRequest.of(0, 1);
        Page<Gate> gatePage = gateRepository
                .findFirstAvailableGate(currentTime, pageable);

        if (!gatePage.hasContent()) {
            log.debug("Available gate not found (current time: {}), " +
                    "throwing GateNotFoundException.", currentTime);
            throw new GateNotFoundException("Available gate not found");
        }
        Gate foundGate = gatePage.getContent().get(0);
        parkFlightOnGateAndSave(flightId, foundGate);

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
            throw new GateNotFoundException("Gate not found, ID: " + id);
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
                        new GateNotFoundException("Gate not found, ID: " + id));
    }

    public void deleteById(Integer id) {
        log.trace("Deleting the gate with ID: {}.", id);
        gateRepository.deleteById(id);
    }

    private boolean checkAvailabilityTime(Gate gate) {
        var localTime = LocalTime.now();
        var openingTime = gate.getOpeningTime();
        var closingTime = gate.getClosingTime();
        if (openingTime.isBefore(closingTime)) {
            return !localTime.isBefore(openingTime)
                    && !localTime.isAfter(closingTime);
        }
        return !localTime.isBefore(openingTime)
                || !localTime.isAfter(closingTime);
    }

    public Gate patchGate(Integer gateId, GateDto dto) {
        Gate gate = getGate(gateId);
        if (dto.name() != null) {
            gate.setName(dto.name());
        }
        if (dto.openingTime() != null) {
            gate.setOpeningTime(dto.openingTime());
        }
        if (dto.closingTime() != null) {
            gate.setClosingTime(dto.closingTime());
        }
        return gateRepository.save(gate);
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

    private Flight getFlight(Integer flightId) {
        return flightRepository.findById(flightId)
                .orElseThrow(() -> {
                    log.error("Flight with ID {} not found, " +
                                    "throwing FlightNotFoundException.",
                            flightId);
                    return new FlightNotFoundException("Gate not found, ID:"
                            + flightId);
                });
    }

    private Gate getGate(Integer gateId) {
        return gateRepository.findById(gateId)
                .orElseThrow(() -> {
                    log.error("Gate with ID {} not found, " +
                                    "throwing GateNotFoundException.",
                            gateId);
                    return new GateNotFoundException("Gate not found, ID:"
                            + gateId);
                });
    }

}