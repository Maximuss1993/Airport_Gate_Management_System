package com.maximus.Airport_Gate_Management_System.flights;

import com.maximus.Airport_Gate_Management_System.airports.AirportResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/flights")
    public List<FlightResponseDto> findAllFlights() {
        return flightService.findAllFlights();
    }

    @PostMapping("/flights")
    @ResponseStatus(HttpStatus.CREATED)
    public FlightResponseDto saveFlight(
            @Valid
            @RequestBody
            FlightDto dto
    ) {
        return flightService.saveFlight(dto);
    }

    @GetMapping("/flights/{flight-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public FlightResponseDto findById(
            @PathVariable("flight-id")
            int id
    ) {
        return flightService.findById(id);
    }

    @GetMapping("/flight/search/{flight-date}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<FlightResponseDto> findFlightByDate (
            @PathVariable("flight-date")
            LocalDate arrivalDate
    ) {
        return flightService.findByArrivingDate(arrivalDate);
    }

    @DeleteMapping("/flight/{flight-id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(
            @PathVariable("flight-id")
            Integer id
    ) {
        flightService.deleteById(id);
    }
}
