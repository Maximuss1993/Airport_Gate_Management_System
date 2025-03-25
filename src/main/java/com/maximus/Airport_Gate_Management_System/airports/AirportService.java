package com.maximus.Airport_Gate_Management_System.airports;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AirportService {

    private final AirportRepository airportRepository;
    private final AirportMapper airportMapper = AirportMapper.INSTANCE;

    public AirportService(
            AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public AirportResponseDto saveAirport(AirportDto dto) {

        Airport airport = airportMapper.toAirport(dto);
        Airport savedAirport = airportRepository.save(airport);
        log.trace("Creating new airport with ID: {}.", airport.getId());

        return airportMapper.toAirportResponseDto(savedAirport);
    }

    public List<AirportResponseDto> findAllAirports() {
        return airportRepository
                .findAll()
                .stream()
                .map(airportMapper::toAirportResponseDto)
                .collect(Collectors.toList());
    }

    public AirportResponseDto findById(Integer id) {
        return airportRepository.findById(id)
                .map(airportMapper::toAirportResponseDto)
                .orElseThrow(() ->
                        new EntityNotFoundException("Airport not found, ID: "
                                + id));
    }

    public List<AirportResponseDto> findByLocation (String location) {
        return airportRepository
                .findAllByLocation(location)
                .stream()
                .map(airportMapper::toAirportResponseDto)
                .collect(Collectors.toList());
    }

    public List<AirportResponseDto> findByName (String name) {
        return airportRepository
                .findAllByName(name)
                .stream()
                .map(airportMapper::toAirportResponseDto)
                .collect(Collectors.toList());
    }

    public void deleteById(Integer id) {
        log.trace("Deleting the airport with ID: {}.", id);
        airportRepository.deleteById(id);
    }
}
