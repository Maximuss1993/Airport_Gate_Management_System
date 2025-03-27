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
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
    LocalTime openingTime1 = LocalTime.of(1, 0);
    LocalTime closingTime1 = LocalTime.of(2, 0);
    LocalTime openingTime2 = LocalTime.of(3, 0);
    LocalTime closingTime2 = LocalTime.of(4, 0);
    String gateName1 = "Gate1";
    String gateName2 = "Gate2";


    @BeforeEach
    public void init() {

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

    @Test
    public void GateController_SaveGate_ReturnCreated() throws Exception {
        List<GateResponseDto> mockGates = List.of(responseDto1, responseDto2);
        when(gateService.findAllGates()).thenReturn(mockGates);
        mockMvc.perform(get("/api/gates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name")
                        .value("Gate1"))
                .andExpect(jsonPath("$[0].openingTime")
                        .value("01:00"))
                .andExpect(jsonPath("$[0].closingTime")
                        .value("02:00"))
                .andExpect(jsonPath("$[1].name")
                        .value("Gate2"))
                .andExpect(jsonPath("$[1].openingTime")
                        .value("03:00"))
                .andExpect(jsonPath("$[1].closingTime")
                        .value("04:00"));
    }

}