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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "payed_for")
@ToString
public class PayedFor {

    @Id
    @Column(name = "uid")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uid;

    @ManyToOne
    @JoinColumn(name = "operation_uid", referencedColumnName = "uid")
    private UUID operationUUID;

    @Column(name = "value")
    @NotBlank(message = "Укажите сумму.")
    @Digits(message = "Укажите цифровое значение по второй знак после запятой например: 36,28", integer = 100, fraction = 2)
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
}
