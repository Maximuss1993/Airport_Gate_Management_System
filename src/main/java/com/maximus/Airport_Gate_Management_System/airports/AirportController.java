package com.maximus.Airport_Gate_Management_System.airports;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

  private final AirportService airportService;

  public AirportController(AirportService airportService) {
    this.airportService = airportService;
  }

  @GetMapping()
  public List<AirportResponseDto> findAllAirports() {
    return airportService.findAllAirports();
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public AirportResponseDto saveAirport(@Valid @RequestBody AirportDto dto) {
    return airportService.saveAirport(dto);
  }

  @GetMapping("/{airport-id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public AirportResponseDto findById(@PathVariable("airport-id") int id) {
    return airportService.findById(id);
  }

  @GetMapping("/search/name/{airport-name}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public List<AirportResponseDto> findAirportByName(
      @PathVariable("airport-name") String airportName) {
    return airportService.findByName(airportName);
  }

  @GetMapping("/search/location/{airport-location}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public List<AirportResponseDto> findAirportByLocation(
      @PathVariable("airport-location") String location) {
    return airportService.findByLocation(location);
  }

  @DeleteMapping("/{airport-id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteById(@PathVariable("airport-id") Integer id) {
    airportService.deleteById(id);
  }
}
