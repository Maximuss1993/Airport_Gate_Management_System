package com.maximus.Airport_Gate_Management_System.gates;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Gates")
@Slf4j
@RequestMapping("/api/v1/gates")
@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
public class GateController {

  private final GateService gateService;

  public GateController(GateService gateService) {
    this.gateService = gateService;
  }

  @PreAuthorize("hasAuthority('manager:read') or hasAuthority('user:read')")
  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public List<GateResponseDto> findAllGates() {
    return gateService.findAllGates();
  }

  @PreAuthorize("hasAuthority('manager:read') or hasAuthority('user:read')")
  @GetMapping("/available")
  public ResponseEntity<List<GateResponseDto>> getAvailableGates() {
    List<GateResponseDto> availableGates = gateService
        .getAvailableGates(LocalTime.now());
    return ResponseEntity.ok(availableGates);
  }

  @PreAuthorize("hasAuthority('manager:read') or hasAuthority('user:read')")
  @Operation(summary = "Find available gates at specific time.")
  @GetMapping("/available/{time}")
  public ResponseEntity<List<GateResponseDto>> getAvailableGates(
      @Parameter(description = "Set time for checking free gates.")
      @PathVariable("time")
      @DateTimeFormat(pattern = "HH:mm")
      LocalTime localTime
  ) {
    if (localTime == null)
      return ResponseEntity.badRequest().build();
    List<GateResponseDto> availableGates = gateService
        .getAvailableGates(localTime);
    if (availableGates == null || availableGates.isEmpty())
      return ResponseEntity.notFound().build();
    return ResponseEntity.ok(availableGates);
  }

  @PreAuthorize("hasAuthority('manager:update') or hasAuthority('user:update')")
  @Operation(summary = "Parking flight on specific gate with provided ID.")
  @PostMapping("/park/flight/{flight-id}/gate/{gate-id}")
  public ResponseEntity<String> parkFlightOnGate(
      @Parameter(description = "Set flight id for parking.")
      @PathVariable("flight-id")
      Integer flightId,

      @Parameter(description = "Set available gate id for parking.")
      @PathVariable("gate-id")
      Integer gateId) {

    boolean success = gateService.parkFlightOnGate(flightId, gateId);
    if (success) {
      return ResponseEntity.ok("Flight ID:" + flightId
          + " successfully parked on " + "gate ID: " + gateId + ".");
    } else {
      log.trace("Flight ID: {} is not successfully parked " +
          "at the gate ID: {}.", flightId, gateId);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("The gate is not available.");
    }
  }

  @PreAuthorize("hasAuthority('manager:update') or hasAuthority('user:update')")
  @Operation(summary = "Parking flight on first available gate.")
  @PostMapping("/park/flight/{flight-id}")
  public ResponseEntity<String> parkFlightOnFirstAvailableGate(
      @PathVariable("flight-id") Integer flightId) {

    boolean success = gateService.parkFlightOnFirstAvailableGate(flightId);
    if (success) {
      return ResponseEntity.ok("Flight parked successfully.");
    } else {
      log.trace("Flight ID: {} is not successfully parked because " +
          "there is not any available gate.", flightId);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body("All gates are already occupied.");
    }
  }

  @PreAuthorize("hasAuthority('manager:create')")
  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  public GateResponseDto saveGate(
      @Valid @RequestBody GateDto dto) {
    return gateService.saveGate(dto);
  }

  @PreAuthorize("hasAuthority('manager:update') or hasAuthority('user:update')")
  @Operation(summary = "Parking out flight from the gate.")
  @PostMapping("/park/out/gate/{gate-id}")
  public ResponseEntity<String> parkOutFlightFromGate(
      @PathVariable("gate-id") Integer gateId) {

    if (gateService.isGateFree(gateId)) {
      return ResponseEntity.ok("Gate ID:" + gateId + " is already free!");
    }
    boolean success = gateService.parkOutFlightFromGate(gateId);
    if (success) {
      return ResponseEntity.ok("Flight parked out successfully from " +
          "gate ID: " + gateId + ".");
    } else {
      log.warn("Something went wrong with parking out from gate ID: {}.",
          gateId);
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body("Failed to park out flight from gate " +
              "ID: " + gateId + ". Please try again.");
    }
  }

  @PreAuthorize("hasAuthority('manager:read') or hasAuthority('user:read')")
  @GetMapping("/{gate-id}")
  public ResponseEntity<GateResponseDto> findById(
      @PathVariable("gate-id") Integer id) {

    Optional<GateResponseDto> gateResponse = gateService.findById(id);
    return gateResponse
        .map(response -> ResponseEntity.status(HttpStatus.ACCEPTED).body(response))
        .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

  @PreAuthorize("hasAuthority('manager:update') or hasAuthority('user:update')")
  @PutMapping("/{id}")
  public ResponseEntity<GateResponseDto> updateGate(
      @PathVariable Integer id,
      @RequestBody GateDto gateDto) {
    GateResponseDto updatedGate = gateService.updateGate(id, gateDto);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedGate);
  }

  @PreAuthorize("hasAuthority('admin:update')")
  @PatchMapping("/{id}")
  public ResponseEntity<GateResponseDto> patchGate(
      @PathVariable Integer id,
      @RequestBody GateDto gateDto) {
    GateResponseDto patchedGateResponseDto = gateService.patchGate(id, gateDto);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(patchedGateResponseDto);
  }

  @PreAuthorize("hasAuthority('manager:delete')")
  @DeleteMapping("/{gate-id}")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void deleteById(@PathVariable("gate-id") Integer id) {
    gateService.deleteById(id);
  }
}
