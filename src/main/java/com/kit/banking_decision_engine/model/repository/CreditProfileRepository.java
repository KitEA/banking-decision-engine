package com.kit.banking_decision_engine.model.repository;

import com.kit.banking_decision_engine.model.CreditProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditProfileRepository extends JpaRepository<CreditProfile, String> {
}
