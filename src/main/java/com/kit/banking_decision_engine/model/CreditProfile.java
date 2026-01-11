package com.kit.banking_decision_engine.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "credit_profiles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreditProfile {

    @Id
    @Column(name = "personal_code", length = 20)
    private String personalCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "segment_id", nullable = false)
    private Segment segment;

    public CreditProfile(String personalCode, Segment segment) {
        this.personalCode = personalCode;
        this.segment = segment;
    }
}
