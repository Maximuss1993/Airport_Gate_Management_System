package com.maximus.Airport_Gate_Management_System.gates;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = GateController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class GateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GateService gateService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GateMapper gateMapper;

    private Gate gate;
    private GateResponseDto responseDto;

    @BeforeEach
    public void init() {
//        MockitoAnnotations.openMocks(this);

        gate = Gate.builder()
                .id(1)
                .name("Test Gate")
                .build();

//        responseDto = gateMapper.toGateResponseDto(gate);
        responseDto = GateResponseDto.builder().name("Test Gate").build();

        given(gateMapper.toGateResponseDto(ArgumentMatchers.any(Gate.class)))
                .willReturn(responseDto);

    }

    @Test
    public void GateController_SaveGate_ReturnCreated() throws Exception {
        given(gateService.saveGate(ArgumentMatchers.any()))
                .willAnswer( (invocation)
                        -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/api/gates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(responseDto))
        );

        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }

}