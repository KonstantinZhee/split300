package com.zhe.split300.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "Person")
@NamedEntityGraph(name = "Person.withCompanies", attributeNodes = {
        @NamedAttributeNode(value = "id"),
        @NamedAttributeNode(value = "name"),
        @NamedAttributeNode(value = "email"),
        @NamedAttributeNode(value = "companies", subgraph = "Companies.details"),
        @NamedAttributeNode(value = "ownedCompanies", subgraph = "Companies.details")
}, subgraphs = {
        @NamedSubgraph(name = "Companies.details", attributeNodes = {
                @NamedAttributeNode(value = "id"),
                @NamedAttributeNode(value = "name"),
        })
})
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @NotBlank(message = "Имя не должно быть пустым.")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов.")
    @Column(name = "name")
    private String name;

    @NotNull
    @Email(message = "Введите корректный адрес электронной почты. Пример: me@gmail.com.")
    @Column(name = "email")
    @Size(max = 50, message = "Адрес электронной почты  должен быть до 50 символов.")
    @NotBlank(message = "Поле не должно быть пустым.")
    private String email;

    @ToString.Exclude
    @ManyToMany(mappedBy = "persons")
    private Set<Company> companies = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "owner")
    private Set<Operation> operations = new HashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "personPaidFor")
    private Set<PaidFor> paidForActions = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    @ToString.Exclude
    private Set<Company> ownedCompanies = new HashSet<>();

    @ToString.Exclude
    @ManyToMany(mappedBy = "persons", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Evention> eventions = new HashSet<>();

    public Person(int id) {
        setId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
