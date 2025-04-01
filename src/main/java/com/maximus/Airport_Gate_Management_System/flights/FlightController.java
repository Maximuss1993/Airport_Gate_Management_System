package com.maximus.Airport_Gate_Management_System.flights;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Flights")
@RequestMapping("/api/v1/flights")
@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
public class FlightController {

  private final FlightService flightService;

  public FlightController(FlightService flightService) {
    this.flightService = flightService;
  }

  @PreAuthorize("hasAuthority('manager:read')")
  @GetMapping()
  public List<FlightResponseDto> findAllFlights() {
    return flightService.findAllFlights();
  }

  @PreAuthorize("hasAuthority('manager:create')")
  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public FlightResponseDto saveFlight(@Valid @RequestBody FlightDto dto) {
    return flightService.saveFlight(dto);
  }

  @PreAuthorize("hasAuthority('manager:read')")
  @GetMapping("/{flight-id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public FlightResponseDto findById(@PathVariable("flight-id") Integer id) {
    return flightService.findById(id);
  }

  @PreAuthorize("hasAuthority('manager:delete')")
  @DeleteMapping("/{flight-id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteById(@PathVariable("flight-id") Integer id) {
    flightService.deleteById(id);
  }
}
