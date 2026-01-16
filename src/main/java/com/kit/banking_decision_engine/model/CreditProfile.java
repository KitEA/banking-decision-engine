package com.kit.banking_decision_engine.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "credit_profiles")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreditProfile {

    @Id
    @Column(name = "personal_code", length = 20)
    private String personalCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "segment_id", nullable = false)
    private Segment segment;
}
