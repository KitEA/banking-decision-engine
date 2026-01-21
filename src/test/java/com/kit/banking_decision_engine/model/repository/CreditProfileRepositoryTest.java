package com.kit.banking_decision_engine.model.repository;

import com.kit.banking_decision_engine.config.TestContainerConfig;
import com.kit.banking_decision_engine.model.CreditProfile;
import com.kit.banking_decision_engine.model.Segment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestContainerConfig.class)
class CreditProfileRepositoryTest {

    @Autowired
    private CreditProfileRepository credRep;

    @Autowired
    private SegmentRepository segRep;

    @Test
    void flywayMigrationsRunAndRepositoryWorks() {
        Segment segment3 = segRep.findByCode("SEGMENT_3")
                .orElseThrow();

        CreditProfile profile = new CreditProfile(
                "49002010998",
                segment3
        );

        credRep.save(profile);

        assertThat(credRep.findById("49002010998"))
                .isPresent();
    }
}