package com.maximus.Airport_Gate_Management_System.airports;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AirportService {

    private final AirportRepository airportRepository;
    private final AirportMapper airportMapper;

    public AirportService(
            AirportRepository airportRepository,
            AirportMapper airportMapper) {

        this.airportRepository = airportRepository;
        this.airportMapper = airportMapper;
    }

    public AirportResponseDto saveAirport(AirportDto dto) {

        Airport airport = airportMapper.toAirport(dto);
        Airport savedAirport = airportRepository.save(airport);

        return airportMapper.toAirportResponseDto(savedAirport);
    }

    public List<AirportResponseDto> findAllAirports() {
        return airportRepository
                .findAll()
                .stream()
                .map(airportMapper::toAirportResponseDto)
                .collect(Collectors.toList());
    }

    public AirportResponseDto findById(int id) {
        return airportRepository.findById(id)
                .map(airportMapper::toAirportResponseDto)
                .orElse(null);
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
        airportRepository.deleteById(id);
    }
}
