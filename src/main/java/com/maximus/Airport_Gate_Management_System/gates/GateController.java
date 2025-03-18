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
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/gates")
public class GateController {

    private final GateService gateService;

    public GateController(GateService gateService) {
        this.gateService = gateService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<GateResponseDto> findAllGates() {
        return gateService.findAllGates();
    }

    @GetMapping("/available")
    public ResponseEntity<List<GateResponseDto>> getAvailableGates() {
        List<GateResponseDto> availableGates = gateService
                .getAvailableGates(LocalTime.now());
        return ResponseEntity.ok(availableGates);
    }

    @GetMapping("/available/{time}")
    public ResponseEntity<List<GateResponseDto>> getAvailableGates(
            @Parameter(description = "Set time for checking free gates.")
            @PathVariable("time")
            @DateTimeFormat(pattern = "HH:mm") LocalTime localTime
    ) {
        if (localTime == null)
            return ResponseEntity.badRequest().build();

        List<GateResponseDto> availableGates = gateService.getAvailableGates(localTime);

        if (availableGates == null || availableGates.isEmpty())
            return ResponseEntity.notFound().build();

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
            return ResponseEntity.ok("Flight parked successfully.");
        } else {
            log.trace("Flight ID: {} is not successfully parked " +
                            "at the gate ID: {}.", flightId, gateId);
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
            return ResponseEntity.ok("Flight parked successfully.");
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

    @Operation(summary = "Parking out flight from the gate.")
    @PostMapping("/park/out/gate/{gate-id}")
    public ResponseEntity<String> parkOutFlightFromGate(
            @PathVariable("gate-id") Integer gateId) {

        if (gateService.isGateFree(gateId)) {
            return ResponseEntity.ok(
                    String.format("Gate ID:%d is already free!", gateId));
        }

        boolean success = gateService.parkOutFlightFromGate(gateId);
        if (success) {
            return ResponseEntity.ok(
                    String.format("Flight parked out successfully from gate " +
                            "ID:%d.", gateId));
        } else {
            log.warn("Something went wrong with parking out from gate ID: {}.",
                    gateId);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(String.format("Failed to park out flight from gate " +
                            "ID:%d. Please try again.", gateId));
        }
    }

    @GetMapping("/{gate-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Optional<GateResponseDto> findById(
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
