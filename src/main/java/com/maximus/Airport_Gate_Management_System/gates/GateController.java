package com.maximus.Airport_Gate_Management_System.gates;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/gates")
public class GateController {

    private final GateService gateService;

    public GateController(GateService gateService) {
        this.gateService = gateService;
    }

    @PostMapping("/park/{flightId}/{gateId}")
    public ResponseEntity<String> parkFlightOnGate(
            @PathVariable Integer flightId,
            @PathVariable Integer gateId) {

        boolean success = gateService.parkFlightOnGate(flightId, gateId);

        if (success) {
            return ResponseEntity
                    .ok("Flight parked successfully.");

        } else {
            log.info("Flight ID: {} is not successfully parked at the " +
                            "gate ID: {} because it is already occupied.",
                    flightId,
                    gateId);

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Gate is already occupied.");
        }
        //dodaj vremensko ogranicenje
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public GateResponseDto saveGate(
            @Valid
            @RequestBody
            GateDto dto
    ) {
        return gateService.saveGate(dto);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Gate>> getAvailableGates() {

        List<Gate> availableGates = gateService.getAvailableGates();

        return ResponseEntity.ok(availableGates);
    }

    @GetMapping()
    public List<GateResponseDto> findAllGates() {
        return gateService.findAllGates();
    }

    @GetMapping("/{gate-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GateResponseDto findById(
            @PathVariable("gate-id")
            int id
    ) {
        return gateService.findById(id);
    }

    @DeleteMapping("/{gate-id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(
            @PathVariable("gate-id")
            Integer id
    ) {
        gateService.deleteById(id);
    }
}
