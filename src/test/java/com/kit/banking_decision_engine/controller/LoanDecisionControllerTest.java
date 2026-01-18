package com.kit.banking_decision_engine.controller;

import com.kit.banking_decision_engine.dto.DecisionRequest;
import com.kit.banking_decision_engine.dto.DecisionResult;
import com.kit.banking_decision_engine.exception.UnknownValueException;
import com.kit.banking_decision_engine.service.DecisionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanDecisionController.class)
class LoanDecisionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DecisionService decisionEngineService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnDecisionResultWhenEverythingIsOk() throws Exception {
        DecisionRequest request = new DecisionRequest(
                "49002010976",
                4000,
                24
        );

        when(decisionEngineService.evaluate(request))
                .thenReturn(new DecisionResult(true, 6000, 36));

        mockMvc.perform(post("/api/loan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.approved").value(true))
                .andExpect(jsonPath("$.amount").value(6000));
    }

    @Test
    @SuppressWarnings("DataFlowIssue")
    void shouldReturnValidationErrorWhenValidationFails() throws Exception {
        DecisionRequest request = new DecisionRequest(
                "",
                1000,
                6
        );

        mockMvc.perform(post("/api/loan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void shouldReturnUnknownValueErrorWhenNonExistingPersonalCode() throws Exception {
        DecisionRequest request = new DecisionRequest(
                "49002011976",
                4000,
                24
        );

        when(decisionEngineService.evaluate(any()))
                .thenThrow(new UnknownValueException("Unknown personal code"));

        mockMvc.perform(post("/api/loan")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("UNKNOWN_VALUE"));
    }

    @Test
    void shouldReturn500ErrorWhenUnexpectedException() throws Exception {
        // given: service throws an unexpected runtime exception
        when(decisionEngineService.evaluate(any()))
                .thenThrow(new RuntimeException("DB down"));

        DecisionRequest request = new DecisionRequest(
                "49002010998",
                4000,
                24
        );

        // when + then
        mockMvc.perform(post("/api/loan") // <-- adjust to your actual endpoint
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value(
                        "An unexpected error occurred. Please try again later."
                ));
    }
}