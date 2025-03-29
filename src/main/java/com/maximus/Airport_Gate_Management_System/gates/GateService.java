package com.maximus.Airport_Gate_Management_System.gates;

import com.maximus.Airport_Gate_Management_System.exceptions.FlightNotFoundException;
import com.maximus.Airport_Gate_Management_System.exceptions.GateNotFoundException;
import com.maximus.Airport_Gate_Management_System.exceptions.GateOccupiedException;
import com.maximus.Airport_Gate_Management_System.exceptions.GateUnavailableTimeException;
import com.maximus.Airport_Gate_Management_System.flights.Flight;
import com.maximus.Airport_Gate_Management_System.flights.FlightRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
public class GateService {

    private final GateRepository gateRepository;
    private final FlightRepository flightRepository;

    private final GateMapper gateMapper = GateMapper.INSTANCE;

    public GateService(GateRepository gateRepository,
                       FlightRepository flightRepository) {
        this.gateRepository = gateRepository;
        this.flightRepository = flightRepository;
    }

    public GateResponseDto saveGate(@Valid GateDto gateDto) {
        Gate gate = gateMapper.toGate(gateDto);
        Gate savedGate = gateRepository.save(gate);
        return gateMapper.toGateResponseDto(savedGate);
    }

    public List<GateResponseDto> getAvailableGates(LocalTime localTime) {
        return gateRepository
                .findAllAvailableGates(localTime)
                .stream()
                .map(gateMapper::toGateResponseDto)
                .collect(Collectors.toList());
    }

    public List<GateResponseDto> findAllGates() {
        return gateRepository
                .findAll()
                .stream()
                .map(gateMapper::toGateResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<GateResponseDto> findById(Integer id) {
        return Optional.ofNullable(gateRepository.findById(id)
                .map(gateMapper::toGateResponseDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Gate not found, ID: " + id)));
    }

    public void deleteById(Integer id) {
        gateRepository.deleteById(id);
    }

    @Transactional
    public GateResponseDto updateGate(Integer id, @Valid GateDto dto) {
        Gate gate = getGate(id);
        gateMapper.updateGateFromDto(dto, gate);
        gateRepository.save(gate);
        return gateMapper.toGateResponseDto(gate);
    }

    @Transactional
    public GateResponseDto patchGate(Integer id, GateDto dto) {
        if (dto == null) {
            log.error("GateDto is null for patch operation.");
            throw new IllegalArgumentException("GateDto cannot be null");
        }
        Gate gate = getGate(id);
        if (dto.name() != null)
            gate.setName(dto.name());
        if (dto.openingTime() != null)
            gate.setOpeningTime(dto.openingTime());
        if (dto.closingTime() != null) {
            gate.setClosingTime(dto.closingTime());
        }
        gateRepository.save(gate);
        return gateMapper.toGateResponseDto(gate);
    }

    @Transactional
    public boolean parkFlightOnGate(Integer flightId, Integer gateId) {
        Gate gate = getGate(gateId);
        checkGateAvailabilityTimeAndOccupation(gate);
        parkFlightOnGateAndSave(flightId, gate);
        log.debug("Flight with ID: {} successfully parked at Gate with ID: {}",
                flightId, gateId);
        return true;
    }

    @Transactional
    public boolean parkFlightOnFirstAvailableGate(Integer flightId) {
        var currentTime = LocalTime.now();
        Gate foundGate = getFirstAvailableGate(currentTime);
        parkFlightOnGateAndSave(flightId, foundGate);
        log.debug("Flight with ID: {} successfully parked at the first " +
                "available gate (ID: {}).", flightId, foundGate.getId());
        return true;
    }

    @Transactional
    public boolean parkOutFlightFromGate(Integer gateId) {
        try {
            gateRepository.parkOutFlightFromGate(gateId);
            log.debug("Successfully parked out the flight from gate ID: {}",
                    gateId);
            return true;
        } catch (RuntimeException e) {
            log.debug("Error while parking out flight from gate ID: {}. " +
                    "Error: {}", gateId, e.getMessage());
            return false;
        }
    }

    public boolean isGateFree(Integer gateId) {
        Gate foundGate = getGate(gateId);
        return (foundGate.getFlight() == null);
    }

    protected boolean checkAvailabilityTime(Gate gate) {
        var localTime = LocalTime.now();
        var openingTime = gate.getOpeningTime();
        var closingTime = gate.getClosingTime();
        if (openingTime.isBefore(closingTime))
            return ( !localTime.isBefore(openingTime)
                    && !localTime.isAfter(closingTime) );
        return ( !localTime.isBefore(openingTime)
                || !localTime.isAfter(closingTime) );
    }

    protected void checkGateAvailabilityTimeAndOccupation(Gate gate) {
        if (gate.getFlight() != null)
            throw new GateOccupiedException("Gate with ID: "
                    + gate.getId() + " is already occupied!");
        if (!checkAvailabilityTime(gate))
            throw new GateUnavailableTimeException("Gate with ID: "
                    + gate.getId() + " is currently unavailable!");
    }

    private Gate getFirstAvailableGate(LocalTime currentTime) {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Gate> gatePage = gateRepository
                .findFirstAvailableGate(currentTime, pageable);
        if (gatePage.isEmpty())
            throw new GateUnavailableTimeException("No available gates found " +
                    "at the current time: " + currentTime);
        return gatePage.getContent().get(0);
    }

    private Flight getFlight(Integer flightId) {
        return flightRepository.findById(flightId).orElseThrow(() ->
                new FlightNotFoundException("Flight not found, ID: " + flightId));
    }

    protected Gate getGate(Integer gateId) {
        return gateRepository.findById(gateId).orElseThrow(() ->
                new GateNotFoundException("Gate not found, ID:" + gateId));
    }

    protected void parkFlightOnGateAndSave(Integer flightId, Gate gate) {
        Flight flight = getFlight(flightId);
        flight.setGate(gate);
        gate.setFlight(flight);
        flightRepository.save(flight);
        gateRepository.save(gate);
        log.debug("Flight number: {} successfully assigned to gate: {}.",
                flight.getFlightNumber(), gate.getName());
    }
}