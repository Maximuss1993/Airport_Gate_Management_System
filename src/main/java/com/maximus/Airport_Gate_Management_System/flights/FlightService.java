package com.maximus.Airport_Gate_Management_System.flights;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FlightService {

  private final FlightRepository flightRepository;

  private final FlightMapper flightMapper = FlightMapper.INSTANCE;

  public FlightService(
      FlightRepository flightRepository) {
    this.flightRepository = flightRepository;
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

  public FlightResponseDto findById(Integer id) {
    return flightRepository.findById(id)
        .map(flightMapper::toFlightResponseDto)
        .orElseThrow(() ->
            new EntityNotFoundException("Flight not found, ID: "
                + id));
  }

  public void deleteById(Integer id) {
    log.trace("Deleting the flight, ID: {}.", id);
    flightRepository.deleteById(id);
  }

}
