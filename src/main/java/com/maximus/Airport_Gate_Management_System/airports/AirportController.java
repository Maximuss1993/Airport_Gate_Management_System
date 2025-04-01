package com.maximus.Airport_Gate_Management_System.airports;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Airports")
@RequestMapping("/api/v1/airports")
@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
public class AirportController {

  private final AirportService airportService;

  public AirportController(AirportService airportService) {
    this.airportService = airportService;
  }

  @PreAuthorize("hasAuthority('manager:read')")
  @GetMapping()
  public List<AirportResponseDto> findAllAirports() {
    return airportService.findAllAirports();
  }

  @PreAuthorize("hasAuthority('manager:create')")
  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public AirportResponseDto saveAirport(@Valid @RequestBody AirportDto dto) {
    return airportService.saveAirport(dto);
  }

  @PreAuthorize("hasAuthority('manager:read')")
  @GetMapping("/{airport-id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public AirportResponseDto findById(@PathVariable("airport-id") int id) {
    return airportService.findById(id);
  }

  @PreAuthorize("hasAuthority('manager:read')")
  @GetMapping("/search/name/{airport-name}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public List<AirportResponseDto> findAirportByName(
      @PathVariable("airport-name") String airportName) {
    return airportService.findByName(airportName);
  }

  @PreAuthorize("hasAuthority('manager:read')")
  @GetMapping("/search/location/{airport-location}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public List<AirportResponseDto> findAirportByLocation(
      @PathVariable("airport-location") String location) {
    return airportService.findByLocation(location);
  }

  @PreAuthorize("hasAuthority('manager:delete')")
  @DeleteMapping("/{airport-id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteById(@PathVariable("airport-id") Integer id) {
    airportService.deleteById(id);
  }
}
