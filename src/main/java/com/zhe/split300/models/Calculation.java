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
    @Column(name = "calculation_id")
    @GeneratedValue
    @UuidGenerator
    private UUID uid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evention_uid", nullable = false, referencedColumnName = "uid")
    @ToString.Exclude
    private Evention evention;

    @ManyToOne
    @JoinColumn(name = "from_person_id", referencedColumnName = "id")
    private Person fromPerson;

    @ManyToOne
    @JoinColumn(name = "to_person_id", referencedColumnName = "id")
    private Person toPerson;

    @Column(name = "calculation_value")
    @Digits(integer = 100, fraction = 2)
    private BigDecimal value;

    public Calculation(Evention evention, Person fromPerson, Person toPerson, BigDecimal value) {
        this.evention = evention;
        this.fromPerson = fromPerson;
        this.toPerson = toPerson;
        this.value = value;
    }

}
