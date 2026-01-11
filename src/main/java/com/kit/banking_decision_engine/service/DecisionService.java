package com.kit.banking_decision_engine.service;

import com.kit.banking_decision_engine.dto.DecisionRequest;
import com.kit.banking_decision_engine.dto.DecisionResult;
import com.kit.banking_decision_engine.model.CreditProfile;
import com.kit.banking_decision_engine.model.repository.CreditProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DecisionService {
    private static final int MIN_AMOUNT = 2000;
    private static final int MAX_AMOUNT = 10000;
    private static final int MIN_PERIOD = 12;
    private static final int MAX_PERIOD = 60;
    private static final int AMOUNT_STEP = 500;

    private final CreditProfileRepository creditProfileRepository;

    public DecisionResult evaluate(DecisionRequest request) {
        // think about processing exception
        CreditProfile profile = loadCreditProfile(request.personalCode());
        int creditModifier = profile.getSegment().getCreditModifier();

        if (hasDebt(creditModifier)) {
            return reject();
        }

        int startPeriod = normalizePeriod(request.loanPeriod());

        return findDecision(creditModifier, startPeriod);
    }

    private CreditProfile loadCreditProfile(String personalCode) {
        return creditProfileRepository
                .findById(personalCode)
                .orElseThrow(() -> new IllegalArgumentException("Unknown personal code"));
    }

    private static boolean hasDebt(int creditModifier) {
        return creditModifier == 0;
    }

    private static DecisionResult reject() {
        return new DecisionResult(false, 0, 0);
    }

    private int normalizePeriod(int requestedPeriod) {
        if (requestedPeriod > MAX_PERIOD) {
            throw new IllegalArgumentException("Loan period exceeds maximum");
        }

        return Math.max(requestedPeriod, MIN_PERIOD);
    }

    private DecisionResult findDecision(int creditModifier, int startPeriod) {
        DecisionResult decision = tryPeriod(creditModifier, startPeriod);

        if (decision.approved()) {
            return decision;
        }

        for (int period = startPeriod + 1; period <= MAX_PERIOD; period++) {
            decision = tryPeriod(creditModifier, period);

            if (decision.approved()) {
                return decision;
            }
        }

        return reject();
    }

    private DecisionResult tryPeriod(int creditModifier, int period) {
        int amount = findMaxApprovedAmountForPeriod(creditModifier, period);

        if (amount > 0) {
            return approve(amount, period);
        }

        return reject();
    }

    private int findMaxApprovedAmountForPeriod(int creditModifier, int period) {
        int bestAmount = 0;

        for (int amount = MIN_AMOUNT; amount <= MAX_AMOUNT; amount += AMOUNT_STEP) {

            if (isApproved(creditModifier, amount, period)) {
                bestAmount = amount;
            }
        }

        return bestAmount;
    }

    // think about parameters passing, maybe wrap in an object?
    private boolean isApproved(int creditModifier, int loanAmount, int period) {
        double score = (double) creditModifier / loanAmount * period;

        return score >= 1.0;
    }

    private static DecisionResult approve(
            int amount,
            int period
    ) {
        return new DecisionResult(true, amount, period);
    }
}
