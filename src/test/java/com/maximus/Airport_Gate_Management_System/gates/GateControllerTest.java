package com.maximus.Airport_Gate_Management_System.gates;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(GateController.class)
class GateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private GateService gateService;

    @InjectMocks
    private GateController gateController;

    @Autowired
    private ObjectMapper objectMapper;

    private Gate gate1;
    private Gate gate2;
    private GateResponseDto responseDto1;
    private GateResponseDto responseDto2;

    @BeforeEach
    public void init() {
        gate1 = Gate.builder()
                .name("Test Gate 1")
                .openingTime(LocalTime.of(1,0))
                .closingTime(LocalTime.of(2,0))
                .build();
        gate2 = Gate.builder()
                .name("Test Gate 2")
                .openingTime(LocalTime.of(3,0))
                .closingTime(LocalTime.of(4,0))
                .build();
        responseDto1 = GateResponseDto.builder()
                .name(gate1.getName())
                .openingTime(gate1.getOpeningTime())
                .closingTime(gate1.getClosingTime())
                .build();
        responseDto2 = GateResponseDto.builder()
                .name(gate2.getName())
                .openingTime(gate2.getOpeningTime())
                .closingTime(gate2.getClosingTime())
                .build();
}

    @Test
    public void GateController_SaveGate_ReturnCreated() throws Exception {
            var response = List.of(responseDto1, responseDto2);

        when(gateService.findAllGates()).thenReturn(response);

        //probaj ovo
//        MockHttpServletResponse response = mvc.perform(
//                        get("/superheroes/?name=RobotMan")
//                                .accept(MediaType.APPLICATION_JSON))
//                .andReturn().getResponse();



//        mockMvc.perform(get("/api/gates"))
//                .andExpect(status().isOk())
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().string(response));
//      TODO: JSON se vraca postavi tako i proveri mapper !


        //probaj ovo
//        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
//        assertThat(response.getContentAsString()).isEqualTo(
//                jsonSuperHero.write(new SuperHero("Rob", "Mannon", "RobotMan")).getJson()
//        );
    }

}