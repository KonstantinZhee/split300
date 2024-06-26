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
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "paid_for")
@ToString
public class PaidFor {

    @Id
    @Column(name = "uid")
    @GeneratedValue
    @UuidGenerator
    private UUID uid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_uid", referencedColumnName = "uid")
    @ToString.Exclude
    private Operation operation;

    @Column(name = "value")
    @NotBlank(message = "Укажите сумму.")
    @Digits(message = "Укажите цифровое значение по второй знак после точки например: 36.28",
            integer = 100, fraction = 2)
    private BigDecimal value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    @ToString.Exclude
    private Person personPaidFor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaidFor paidFor = (PaidFor) o;
        return Objects.equals(uid, paidFor.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uid);
    }
}
