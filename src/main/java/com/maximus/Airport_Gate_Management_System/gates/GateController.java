package com.maximus.Airport_Gate_Management_System.gates;

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

    @PostMapping("/park/flight/{flightId}/gate/{gateId}")
    public ResponseEntity<String> parkFlightOnGate(
            @PathVariable Integer flightId,
            @PathVariable Integer gateId) {
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

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public GateResponseDto saveGate(
            @Valid @RequestBody GateDto dto
    ) {
        return gateService.saveGate(dto);
    }

    @GetMapping("/available")
    public ResponseEntity<List<Gate>> getAvailableGates() {
        List<Gate> availableGates = gateService.getAvailableGates();
        return ResponseEntity.ok(availableGates);
    }

    @GetMapping("/available/{time}")
    public ResponseEntity<List<Gate>> getAvailableGates(
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

    @GetMapping()
    public List<GateResponseDto> findAllGates() {
        return gateService.findAllGates();
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

    @DeleteMapping("/{gate-id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(
            @PathVariable("gate-id") Integer id
    ) {
        gateService.deleteById(id);
    }

//    @PostMapping("update-opening/{id}/{local-time}")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public void updateGateOpeningTime(
//            @PathVariable("id") Integer id,
//            @PathVariable("local-time") LocalTime localTime
//    ) {
//        gateService.updateOpeningTime(id, localTime);
//    }
//
//    @PostMapping("update-closing/{id}/{local-time}")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public void updateGateClosingTime(
//            @PathVariable("id") Integer id,
//            @PathVariable("local-time") LocalTime localTime
//    ) {
//        gateService.updateClosingTime(id, localTime);
//    }

    //PREPRAVI DA IMAA PATCH DTO ZBOG VALIDATORA!!!
    @PatchMapping("/{id}")
    public ResponseEntity<Gate> patchGate(
            @PathVariable Integer id,
            @RequestBody @Valid GateDto gateDto) {

        Gate patchedGate = gateService.patchGate(id, gateDto);
        return ResponseEntity.ok(patchedGate);
    }
}
