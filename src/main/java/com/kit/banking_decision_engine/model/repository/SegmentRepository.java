package com.kit.banking_decision_engine.model.repository;

import com.kit.banking_decision_engine.model.Segment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SegmentRepository extends JpaRepository<Segment, Long> {
    Optional<Segment> findByCode(String code);
}
