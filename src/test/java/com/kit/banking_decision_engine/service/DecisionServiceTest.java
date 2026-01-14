package com.kit.banking_decision_engine.service;

import com.kit.banking_decision_engine.dto.DecisionRequest;
import com.kit.banking_decision_engine.dto.DecisionResult;
import com.kit.banking_decision_engine.model.CreditProfile;
import com.kit.banking_decision_engine.model.Segment;
import com.kit.banking_decision_engine.model.repository.CreditProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DecisionServiceTest {
    @Mock
    private CreditProfileRepository creditProfileRepository;

    @InjectMocks
    private DecisionService decisionService;

    @Test
    void shouldRejectPersonWithDebt() {
        // given
        givenProfile("49002010965", 0);

        // when
        DecisionResult result = decisionService.evaluate(
                new DecisionRequest("49002010965", 4000, 24)
        );

        //then
        assertThat(result.approved()).isFalse();
    }

    @Test
    void shouldApproveWithinRequestedPeriod() {
        // given
        givenProfile("49002010998", 1000);

        // when
        DecisionResult result = decisionService.evaluate(
                new DecisionRequest("49002010998", 4000, 24));

        // then
        assertThat(result.approved()).isTrue();
        assertThat(result.approvedAmount()).isEqualTo(10000);
        assertThat(result.approvedPeriod()).isEqualTo(24);
    }

    @Test
    void shouldIncreasePeriodIfNeeded() {
        // given
        givenProfile("49002010976", 100);

        // when
        DecisionResult result = decisionService.evaluate(
                new DecisionRequest("49002010976", 1000, 12)
        );

        // then
        assertThat(result.approved()).isTrue();
        assertThat(result.approvedPeriod()).isGreaterThan(12);
    }

    @Test
    void shouldEnforceMinimumPeriod() {
        // give
        givenProfile("49002010998", 1000);

        // when
        DecisionResult result = decisionService.evaluate(
                new DecisionRequest("49002010998", 4000, 6));

        // then
        assertThat(result.approved()).isTrue();
        assertThat(result.approvedPeriod()).isGreaterThanOrEqualTo(12);
    }

    @Test
    void shouldThrowAnExceptionWhenPersonalCodeIsUnknown() {
        // give/when
        when(creditProfileRepository.findById("49002010953"))
                .thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() ->
                decisionService.evaluate(
                        new DecisionRequest("49002010953", 4000, 24)
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown personal code");
    }

    @Test
    void shouldThrowAnExceptionWhenLoanPeriodExceedsMaximum() {
        // give/when
        givenProfile("49002010998", 1000);

        // then
        assertThatThrownBy(() ->
                decisionService.evaluate(
                        new DecisionRequest("49002010998", 4000, 72)
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Loan period exceeds maximum");
    }

    private void givenProfile(String code, int creditModifier) {
        Segment segment = new Segment(1L, "SEG", creditModifier);
        CreditProfile profile = new CreditProfile(code, segment);

        when(creditProfileRepository.findById(code))
                .thenReturn(Optional.of(profile));
    }
}