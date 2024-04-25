package com.zhe.split300.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Operation")
public class Operation {

    @Id
    @Column(name = "uid")
    @NotNull
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uid;

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "evention_uid", referencedColumnName = "uid")
    private UUID eventionUUID;

    @NotBlank(message = "Поле не должно быть пустым.")
    @Column(name = "name")
    private String name;

    @Column(name = "value")
    @NotBlank(message = "Укажите сумму.")
    @Digits(message = "Укажите цифровое значение по второй знак после запятой например: 36,28", integer = 100, fraction = 2)
    private BigDecimal value;

    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MMMM/yyyy - HH:mm:ss")
    private Date time;

    @Column(name = "note")
    @Size(min = 5, max = 100, message = "Комментарий должен быть от 5 до 100 символов.")
    private String note;
}
