package com.maximus.Airport_Gate_Management_System.gates;

import com.maximus.Airport_Gate_Management_System.customExceptions.FlightNotFoundException;
import com.maximus.Airport_Gate_Management_System.customExceptions.GateNotFoundException;
import com.maximus.Airport_Gate_Management_System.flights.Flight;
import com.maximus.Airport_Gate_Management_System.flights.FlightRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
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

    //DOBRO PROVERI I DODAJ JOS JEDNU F-JU da parkira na prvi slobodan gate

    @Transactional
    public boolean parkFlightOnGate(Integer flightId, Integer gateId) {

        Gate gate = gateRepository.findById(gateId)
                .orElseThrow(() -> {
                    log.error("Gate with ID {} not found, " +
                            "throwing GateNotFoundException.",
                            gateId);
                    return new GateNotFoundException("Gate not found, ID:"
                            + gateId);
                });

        if (gate.getFlight() != null) {
            log.debug("Gate ID: {} is occupied by another flight!", gateId);
            return false;
        }

        if (!checkAvailabilityTime(gate)) {
            log.debug("Gate ID: {} is not available due to " +
                    "limited working time!", gateId);
            return false;
        }

        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> {
                    log.error("Flight with ID {} not found, " +
                            "throwing FlightNotFoundException.", flightId);
                    return new FlightNotFoundException("Flight with ID "
                            + flightId + " not found");
                });

        flight.setGate(gate);
        gate.setFlight(flight);

        flightRepository.save(flight);
        gateRepository.save(gate);

        log.info("Flight number: {} successfully assigned to gate: {}.",
                flight.getFlightNumber(), gate.getName());
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

//    public void updateOpeningTime(Integer gateId, LocalTime localTime) {
//        Gate gate = gateRepository.findById(gateId)
//                .orElseThrow(() -> {
//                    log.debug(
//                            "Gate ID: {} for updating opening time is not found." +
//                            "Throwing GateNotFoundException.",
//                            gateId);
//                    return new GateNotFoundException("Gate for updating not found");
//                });
//        gateRepository.updateGateOpeningTime(gate.getId(), localTime);
//    }
//
//    public void updateClosingTime(Integer gateId, LocalTime localTime) {
//        Gate gate = gateRepository.findById(gateId)
//                .orElseThrow(() -> {
//                    log.debug(
//                            "Gate ID: {} for updating closing time is not found." +
//                                    "Throwing GateNotFoundException.",
//                            gateId);
//                    return new GateNotFoundException("Gate for updating not found");
//                });
//        gateRepository.updateGateClosingTime(gate.getId(), localTime);
//    }

    public Gate patchGate(Integer id, GateDto gateDto) {
        Optional<Gate> gateOptional = gateRepository.findById(id);
        if (gateOptional.isEmpty()) {
            throw new GateNotFoundException("Gate with id" + id + "not found");
        }
        Gate gate = gateOptional.get();
        if (gateDto.name() != null) {
            gate.setName(gateDto.name());
        }
        if (gateDto.openingTime() != null) {
            gate.setOpeningTime(gateDto.openingTime());
        }
        if (gateDto.closingTime() != null) {
            gate.setClosingTime(gateDto.closingTime());
        }

        return gateRepository.save(gate);
    }

}