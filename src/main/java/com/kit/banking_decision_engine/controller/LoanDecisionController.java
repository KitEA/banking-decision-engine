package com.kit.banking_decision_engine.controller;

import com.kit.banking_decision_engine.dto.DecisionRequest;
import com.kit.banking_decision_engine.dto.DecisionResponse;
import com.kit.banking_decision_engine.dto.DecisionResult;
import com.kit.banking_decision_engine.service.DecisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/loan")
public class LoanDecisionController {

    private final DecisionService decisionEngineService;

    @PostMapping
    public DecisionResponse decide(@RequestBody DecisionRequest request) {
        DecisionResult result = decisionEngineService.evaluate(request);

        return new DecisionResponse(
                result.approved(),
                result.approvedAmount()
        );
    }
}
