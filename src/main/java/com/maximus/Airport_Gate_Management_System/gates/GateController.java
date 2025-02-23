package com.maximus.Airport_Gate_Management_System.gates;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GateController {

    private final GateService gateService;

    public GateController(GateService gateService) {
        this.gateService = gateService;
    }

    @GetMapping("/gates")
    public List<GateResponseDto> findAllGates() {
        return gateService.findAllGates();
    }

    @GetMapping("/gates/{gate-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GateResponseDto findById(
            @PathVariable("gate-id")
            int id
    ) {
        return gateService.findById(id);
    }

    @GetMapping("/gates/search/{gate-availability}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<GateResponseDto> findGateByAvailability (
            @PathVariable("gate-availability")
            boolean available
    ) {
        return gateService.findAllByAvailability(available);
    }

    @DeleteMapping("/gates/{gate-id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(
            @PathVariable("gate-id")
            Integer id
    ) {
        gateService.deleteById(id);
    }
}
