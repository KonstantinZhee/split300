package com.zhe.split300.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
@Table(name = "person_balance", indexes = {@Index(name = "personIndex", columnList = "person_id")})
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

    public void setBalance(@Digits(integer = 100, fraction = 4) BigDecimal balance) {
        if (balance.compareTo(new BigDecimal("-0.01")) >= 0 && balance.compareTo(new BigDecimal("0.01")) <= 0) {
            this.balance = BigDecimal.ZERO;
        } else {
            this.balance = balance;
        }
    }

    @Column(name = "balance_value")
    @Digits(integer = 100, fraction = 4)
    private BigDecimal balance;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonBalance that)) return false;
        return Objects.equals(person, that.person);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(person);
    }

    public PersonBalance(Evention evention, BigDecimal value, Person person) {
        this.evention = evention;
        this.balance = value;
        this.person = person;
    }
}
