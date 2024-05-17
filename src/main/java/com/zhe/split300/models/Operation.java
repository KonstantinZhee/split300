package com.zhe.split300.models;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "Operation")
@ToString
@NamedEntityGraph(name = "Operation.details", includeAllAttributes = true, attributeNodes = {
        @NamedAttributeNode(value = "evention", subgraph = "evention.name"),
        @NamedAttributeNode(value = "owner", subgraph = "person.details"),
        @NamedAttributeNode(value = "operationBalances", subgraph = "operationBalances.details"),
}, subgraphs = {
        @NamedSubgraph(name = "evention.name", attributeNodes = {
                @NamedAttributeNode(value = "name"),
                @NamedAttributeNode(value = "uid")
        }),
        @NamedSubgraph(name = "person.details", attributeNodes = {
                @NamedAttributeNode(value = "name"),
                @NamedAttributeNode(value = "email"),
                @NamedAttributeNode(value = "id")
        }),
        @NamedSubgraph(name = "operationBalances.details", attributeNodes = {
                @NamedAttributeNode(value = "person", subgraph = "person.details"),
                @NamedAttributeNode(value = "balance")
        })
})
public class Operation {

    @Id
    @Column(name = "uid", updatable = false, nullable = false)
    @GeneratedValue
    @UuidGenerator
    private UUID uid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evention_uid", referencedColumnName = "uid")
    @ToString.Exclude
    private Evention evention;

    @NotNull(message = "Введите имя.")
    @Column(name = "name")
    private String name;

    @Column(name = "time")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MMMM/yyyy - HH:mm")
    private Date time;

    @Column(name = "note")
    @Size(max = 100, message = "Комментарий должен быть от 5 до 100 символов.")
    private String note;

    @Column(name = "value")
    @NotNull(message = "Укажите сумму.")
    @Digits(message = "Укажите цифровое значение по второй знак после запятой например: 36,28",
            integer = 100, fraction = 2)
    private BigDecimal value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    @ToString.Exclude
    private Person owner;

    @ToString.Exclude
    @OneToMany(mappedBy = "operation", cascade = CascadeType.ALL)
    private Set<PaidFor> paidForActions;

    @ToString.Exclude
    @OneToMany(mappedBy = "operation", cascade = CascadeType.ALL)
    private Set<OperationBalance> operationBalances;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return Objects.equals(uid, operation.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uid);
    }
}
