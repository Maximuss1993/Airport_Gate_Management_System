package com.maximus.Airport_Gate_Management_System.gates;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GateService {

    private final GateRepository gateRepository;
    private final GateMapper gateMapper;

    public GateService(GateRepository gateRepository, GateMapper gateMapper) {
        this.gateRepository = gateRepository;
        this.gateMapper = gateMapper;
    }

    public GateResponseDto saveGate(GateDto dto) {

        Gate gate = gateMapper.toGate(dto);
        Gate savedGate = gateRepository.save(gate);

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

    public List<GateResponseDto> findAllByAvailability(boolean available) {
        return gateRepository
                .findAllByAvailable(available)
                .stream()
                .map(gateMapper::toGateResponseDto)
                .collect(Collectors.toList());
    }

    public void deleteById(Integer id) {
        gateRepository.deleteById(id);
    }

}