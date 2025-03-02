package com.maximus.Airport_Gate_Management_System.airports;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping("/airports")
    public List<AirportResponseDto> findAllAirports() {
        return airportService.findAllAirports();
    }

    @PostMapping("/airports")
    @ResponseStatus(HttpStatus.CREATED)
    public AirportResponseDto saveAirport(
            @Valid
            @RequestBody
            AirportDto dto
    ) {
        return airportService.saveAirport(dto);
    }

    @GetMapping("/airports/{airport-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AirportResponseDto findById(
            @PathVariable("airport-id")
            int id
    ) {
        return airportService.findById(id);
    }

    @GetMapping("/airports/search/{airport-name}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<AirportResponseDto> findAirportByName (
            @PathVariable("airport-name")
            String airportName
    ) {
        return airportService.findByName(airportName);
    }

    @DeleteMapping("/airports/{airport-id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(
            @PathVariable("airport-id")
            Integer id
    ) {
        airportService.deleteById(id);
    }
}
