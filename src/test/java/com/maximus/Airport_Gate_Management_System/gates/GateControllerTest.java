package com.maximus.Airport_Gate_Management_System.gates;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(GateController.class)
class GateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GateService gateService;

    @InjectMocks
    private GateController gateController;

    @Autowired
    private ObjectMapper objectMapper;

    private GateResponseDto responseDto1;
    private GateResponseDto responseDto2;
    String gateName1 = "Gate1";
    LocalTime openingTime1 = LocalTime.of(1, 0);
    LocalTime closingTime1 = LocalTime.of(2, 0);
    String gateName2 = "Gate2";
    LocalTime openingTime2 = LocalTime.of(3, 0);
    LocalTime closingTime2 = LocalTime.of(4, 0);

    @BeforeEach
    public void setUp() {
        responseDto1 = GateResponseDto.builder()
                .name(gateName1)
                .openingTime(openingTime1)
                .closingTime(closingTime1)
                .flight(null)
                .build();
        responseDto2 = GateResponseDto.builder()
                .name(gateName2)
                .openingTime(openingTime2)
                .closingTime(closingTime2)
                .flight(null)
                .build();
    }

    /*  Take care of:
        1. HTTP method
        2. Request path
        3. Return type
        4. Input (request body / param)
        5. HTTP response status code
        6. HTTP response body
    */

    @Test
    public void should_return_all_gates() throws Exception {
        List<GateResponseDto> mockGates = List.of(responseDto1, responseDto2);
        when(gateService.findAllGates()).thenReturn(mockGates);
        mockMvc.perform(get("/api/gates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Gate1"))
                .andExpect(jsonPath("$[0].openingTime").value("01:00"))
                .andExpect(jsonPath("$[0].closingTime").value("02:00"))
                .andExpect(jsonPath("$[1].name").value("Gate2"))
                .andExpect(jsonPath("$[1].openingTime").value("03:00"))
                .andExpect(jsonPath("$[1].closingTime").value("04:00"));
    }

    @Test
    public void should_return_all_available_gates() throws Exception {
        List<GateResponseDto> mockGates = List.of(responseDto1, responseDto2);
        when(gateService.getAvailableGates(any(LocalTime.class)))
                .thenReturn(mockGates);
        mockMvc.perform(get("/api/gates/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Gate1"))
                .andExpect(jsonPath("$[0].openingTime").value("01:00"))
                .andExpect(jsonPath("$[0].closingTime").value("02:00"))
                .andExpect(jsonPath("$[1].name").value("Gate2"))
                .andExpect(jsonPath("$[1].openingTime").value("03:00"))
                .andExpect(jsonPath("$[1].closingTime").value("04:00"));
    }

    @Test
    public void should_save_gate() throws Exception {
        String requestBody = objectMapper.writeValueAsString(responseDto1);
        when(gateService.saveGate(any(GateDto.class))).thenReturn(responseDto1);
        mockMvc.perform(post("/api/gates")
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Gate1"))
                .andExpect(jsonPath("$.openingTime").value("01:00"))
                .andExpect(jsonPath("$.closingTime").value("02:00"));
    }

    @Test
    public void should_find_by_id() throws Exception {
        Integer gateId = 1;
        when(gateService.findById(gateId))
                .thenReturn(Optional.of(responseDto1));
        mockMvc.perform(get("/api/gates/{gate-id}", gateId))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.name").value("Gate1"))
                .andExpect(jsonPath("$.openingTime").value("01:00"))
                .andExpect(jsonPath("$.closingTime").value("02:00"));
    }

    @Test
    public void should_delete_gate_by_id() throws Exception {
        Integer gateId = 1;
        doNothing().when(gateService).deleteById(gateId);
        mockMvc.perform(delete("/api/gates/{gate-id}", gateId))
                .andExpect(status().isAccepted());
        verify(gateService, times(1)).deleteById(gateId);
    }

    @Test
    public void should_update_gate() throws Exception {
        Integer gateId = 1;
        GateResponseDto updatedGateResponseDto = GateResponseDto.builder()
                .name("Updated Gate")
                .build();
        when(gateService.updateGate(eq(gateId), any(GateDto.class)))
                .thenReturn(updatedGateResponseDto);
        mockMvc.perform(put("/api/gates/{id}", gateId)
                        .contentType("application/json")
                        .content("{\"name\": \"Updated Gate\"}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.name").value("Updated Gate"));
        verify(gateService, times(1))
                .updateGate(eq(gateId), any(GateDto.class));
    }

    @Test
    public void should_patch_gate() throws Exception {
        Integer gateId = 1;
        GateResponseDto patchedGateResponseDto = GateResponseDto.builder()
                .name("Patched Gate")
                .build();
        when(gateService.patchGate(eq(gateId), any(GateDto.class)))
                .thenReturn(patchedGateResponseDto);

        mockMvc.perform(patch("/api/gates/{id}", gateId)
                        .contentType("application/json")
                        .content("{\"name\": \"Patched Gate\"}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.name").value("Patched Gate"));

        verify(gateService, times(1))
                .patchGate(eq(gateId), any(GateDto.class));
    }

    @Test
    public void should_return_available_gates_when_time_is_valid() throws Exception {
        List<GateResponseDto> availableGates = List.of(responseDto1, responseDto2);
        when(gateService.getAvailableGates(any(LocalTime.class)))
                .thenReturn(availableGates);
        mockMvc.perform(get("/api/gates/available/{time}", LocalTime.now())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Gate1"))
                .andExpect(jsonPath("$[1].name").value("Gate2"));
    }

    @Test
    public void should_return_not_found_when_no_available_gates() throws Exception {
        when(gateService.getAvailableGates(any(LocalTime.class))).thenReturn(List.of());
        mockMvc.perform(get("/api/gates/available/{time}",
                        LocalTime.of(10,10))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_park_flight_successfully() throws Exception {
        Integer flightId = 101;
        Integer gateId = 5;
        when(gateService.parkFlightOnGate(flightId, gateId)).thenReturn(true);
        mockMvc.perform(post(
                "/api/gates/park/flight/{flight-id}/gate/{gate-id}",
                        flightId, gateId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Flight ID:"
                        + flightId + " successfully parked on " +
                        "gate ID: " + gateId + "."));
        verify(gateService, times(1))
                .parkFlightOnGate(flightId, gateId);
    }

    @Test
    public void should_return_bad_request_when_gate_not_available() throws Exception {
        Integer flightId = 102;
        Integer gateId = 6;
        when(gateService.parkFlightOnGate(flightId, gateId)).thenReturn(false);
        mockMvc.perform(post(
                "/api/gates/park/flight/{flight-id}/gate/{gate-id}",
                        flightId, gateId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("The gate is not available."));
        verify(gateService, times(1)).parkFlightOnGate(flightId, gateId);
    }

    @Test
    public void should_park_flight_on_first_available_gate_successfully() throws Exception {
        Integer flightId = 101;
        when(gateService.parkFlightOnFirstAvailableGate(flightId)).thenReturn(true);
        mockMvc.perform(post("/api/gates/park/flight/{flight-id}", flightId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Flight parked successfully."));
        verify(gateService, times(1)).parkFlightOnFirstAvailableGate(flightId);
    }

    @Test
    public void should_return_bad_request_when_no_gate_is_available() throws Exception {
        Integer flightId = 102;
        when(gateService.parkFlightOnFirstAvailableGate(flightId)).thenReturn(false);
        mockMvc.perform(post("/api/gates/park/flight/{flight-id}", flightId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("All gates are already occupied."));
        verify(gateService, times(1)).parkFlightOnFirstAvailableGate(flightId);
    }

    @Test
    public void should_return_message_if_gate_is_already_free() throws Exception {
        Integer gateId = 1;
        when(gateService.isGateFree(gateId)).thenReturn(true);
        mockMvc.perform(post("/api/gates/park/out/gate/{gate-id}", gateId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Gate ID:1 is already free!"));
        verify(gateService, times(1)).isGateFree(gateId);
    }

    @Test
    public void should_successfully_park_out_flight_from_gate() throws Exception {
        Integer gateId = 1;
        when(gateService.isGateFree(gateId)).thenReturn(false);
        when(gateService.parkOutFlightFromGate(gateId)).thenReturn(true);
        mockMvc.perform(post("/api/gates/park/out/gate/{gate-id}", gateId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "Flight parked out successfully from gate ID: 1.")); // Success message
        verify(gateService, times(1)).isGateFree(gateId);
        verify(gateService, times(1)).parkOutFlightFromGate(gateId);
    }

    @Test
    public void should_return_bad_request_when_parking_out_flight_fails() throws Exception {
        Integer gateId = 1;
        when(gateService.isGateFree(gateId)).thenReturn(false);
        when(gateService.parkOutFlightFromGate(gateId)).thenReturn(false);
        mockMvc.perform(post("/api/gates/park/out/gate/{gate-id}", gateId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(
                        "Failed to park out flight from gate ID: 1. Please try again.")); // Failure message
        verify(gateService, times(1)).isGateFree(gateId);
        verify(gateService, times(1)).parkOutFlightFromGate(gateId);
    }
}