package com.maximus.Airport_Gate_Management_System.gates;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/gates")
public class GateController {

    private final GateService gateService;

    public GateController(GateService gateService) {
        this.gateService = gateService;
    }

    @GetMapping()
    public List<GateResponseDto> findAllGates() {
        return gateService.findAllGates();
    }

    @GetMapping("/available")
    public ResponseEntity<List<Gate>> getAvailableGates() {
        List<Gate> availableGates = gateService.getAvailableGates();
        return ResponseEntity.ok(availableGates);
    }

    @GetMapping("/available/{time}")
    public ResponseEntity<List<Gate>> getAvailableGates(
            @Parameter(description = "Set time for checking free gates.")
            @PathVariable("time")
            @DateTimeFormat(pattern = "HH:mm") LocalTime localTime
    ) {
        if (localTime == null) {
            return ResponseEntity.badRequest().build();
        }
        List<Gate> availableGates = gateService.getAvailableGates(localTime);
        if (availableGates == null || availableGates.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(availableGates);
    }

    @Operation(summary = "Parking flight on specific gate with provided ID.")
    @PostMapping("/park/flight/{flight-id}/gate/{gate-id}")
    public ResponseEntity<String> parkFlightOnGate(
            @Parameter(description = "Set flight id for parking.")
            @PathVariable("flight-id") Integer flightId,
            @Parameter(description = "Set available gate id for parking.")
            @PathVariable("gate-id") Integer gateId) {
        boolean success = gateService.parkFlightOnGate(flightId, gateId);
        if (success) {
            return ResponseEntity
                    .ok("Flight parked successfully.");
        } else {
            log.trace("Flight ID: {} is not successfully parked " +
                            "at the gate ID: {}.",
                    flightId,
                    gateId);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Gate is already occupied.");
        }
    }

    @Operation(summary = "Parking flight on first available gate.")
    @PostMapping("/park/flight/{flight-id}")
    public ResponseEntity<String> parkFlightOnFirstAvailableGate(
            @PathVariable("flight-id") Integer flightId) {

        boolean success = gateService.parkFlightOnFirstAvailableGate(flightId);
        if (success) {
            return ResponseEntity
                    .ok("Flight parked successfully.");
        } else {
            log.trace("Flight ID: {} is not successfully parked because " +
                            "there is not any available gate.",
                    flightId);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("All gates are already occupied.");
        }
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public GateResponseDto saveGate(
            @Valid @RequestBody GateDto dto
    ) {
        return gateService.saveGate(dto);
    }

    @GetMapping("/{gate-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GateResponseDto findById(
            @PathVariable("gate-id") Integer id
    ) {
        return gateService.findById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GateResponseDto updateGate(
            @PathVariable("id") Integer id,
            @Valid @RequestBody GateDto gateDto) {
        return gateService.updateGate(id, gateDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Gate> patchGate(
            @PathVariable Integer id,
            @RequestBody GateDto gateDto) {
        Gate patchedGate = gateService.patchGate(id, gateDto);
        return ResponseEntity.ok(patchedGate);
    }

    @DeleteMapping("/{gate-id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(
            @PathVariable("gate-id") Integer id
    ) {
        gateService.deleteById(id);
    }
}
