package com.zhe.split300.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "calculation")
public class Calculation {

    @Id
    @Column(name = "calculation_id", updatable = false, nullable = false)
    @GeneratedValue
    @UuidGenerator
    private UUID uid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evention_uid", nullable = false, referencedColumnName = "uid")
    @ToString.Exclude
    private Evention evention;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id", insertable=false, updatable=false)
    private Person fromPerson;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id", insertable=false, updatable=false)
    private Person toPerson;

    @Column(name = "calculation_value")
    @Digits(integer = 100, fraction = 2)
    private BigDecimal value;
}
