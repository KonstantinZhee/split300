package com.zhe.split300.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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

    @ManyToOne
    @JoinColumn(name = "operation_uid", referencedColumnName = "uid")
    private Operation operation;

    @Column(name = "value")
    @NotBlank(message = "Укажите сумму.")
    @Digits(message = "Укажите цифровое значение по второй знак после запятой например: 36,28", integer = 100, fraction = 2)
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person personPaidFor;
}
