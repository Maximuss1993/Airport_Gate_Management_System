package com.maximus.Airport_Gate_Management_System.flights;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FlightService {

    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;

    public FlightService(
            FlightRepository flightRepository,
            FlightMapper flightMapper) {

        this.flightRepository = flightRepository;
        this.flightMapper = flightMapper;
    }

    public FlightResponseDto saveFlight(FlightDto dto) {

        Flight flight = flightMapper.toFlight(dto);
        Flight savedFlight = flightRepository.save(flight);
        log.trace("Creating new flight with ID: {}.", flight.getId());

        return flightMapper.toFlightResponseDto(savedFlight);
    }

    public List<FlightResponseDto> findAllFlights() {
        return flightRepository
                .findAll()
                .stream()
                .map(flightMapper::toFlightResponseDto)
                .collect(Collectors.toList());
    }

    public FlightResponseDto findById(int id) {
        return flightRepository.findById(id)
                .map(flightMapper::toFlightResponseDto)
                .orElse(null);
    }

    public List<FlightResponseDto> findByArrivingDate (LocalDate arrivingDate) {
        return flightRepository
                .findAllByArrivingDate(arrivingDate)
                .stream()
                .map(flightMapper::toFlightResponseDto)
                .collect(Collectors.toList());
    }

    public List<FlightResponseDto> findByArrivingTime(LocalTime arrivingTime) {
        return flightRepository
                .findAllByArrivingTime(arrivingTime)
                .stream()
                .map(flightMapper::toFlightResponseDto)
                .collect(Collectors.toList());
    }

    public void deleteById(Integer id) {
        log.trace("Deleting the flight with ID: {}.", id);
        flightRepository.deleteById(id);
    }

}
