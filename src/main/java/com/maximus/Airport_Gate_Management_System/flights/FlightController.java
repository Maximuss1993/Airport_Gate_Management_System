package com.maximus.Airport_Gate_Management_System.flights;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping()
    public List<FlightResponseDto> findAllFlights() {
        return flightService.findAllFlights();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public FlightResponseDto saveFlight(
            @Valid
            @RequestBody
            FlightDto dto
    ) {
        return flightService.saveFlight(dto);
    }

    @GetMapping("/{flight-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public FlightResponseDto findById(
            @PathVariable("flight-id")
            int id
    ) {
        return flightService.findById(id);
    }

    @DeleteMapping("/{flight-id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(
            @PathVariable("flight-id")
            Integer id
    ) {
        flightService.deleteById(id);
    }
}
