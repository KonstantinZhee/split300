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
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "person_balance")
public class PersonBalance {

    @Id
    @Column(name = "person_balance_id")
    @GeneratedValue
    @UuidGenerator
    private UUID uid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evention_uid", nullable = false, referencedColumnName = "uid")
    @ToString.Exclude
    private Evention evention;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    @ToString.Exclude
    private Person person;

    @Column(name = "balance_value")
    @Digits(integer = 100, fraction = 4)
    private BigDecimal balance;

    public PersonBalance(Evention evention, BigDecimal value, Person person) {
        this.evention = evention;
        this.balance = value;
        this.person = person;
    }

}
