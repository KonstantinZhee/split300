package com.zhe.split300.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "Company")
@NamedEntityGraph(name = "Company.withPersonsAndEventions", attributeNodes = {
        @NamedAttributeNode(value = "name"),
        @NamedAttributeNode(value = "id"),
        @NamedAttributeNode(value = "owner", subgraph = "Person.details"),
        @NamedAttributeNode(value = "persons", subgraph = "Person.details"),
        @NamedAttributeNode(value = "eventions", subgraph = "Evention.names"),
}, subgraphs = {
        @NamedSubgraph(name = "Person.details", attributeNodes = {
                @NamedAttributeNode(value = "id"),
                @NamedAttributeNode(value = "name"),
                @NamedAttributeNode(value = "email")
        }),
        @NamedSubgraph(name = "Evention.names", attributeNodes = {
                @NamedAttributeNode(value = "uid"),
                @NamedAttributeNode(value = "name"),
                @NamedAttributeNode(value = "startTime")
        })
})
@NamedEntityGraph(name = "Company.withPersonsAndOwner", attributeNodes = {
        @NamedAttributeNode(value = "name"),
        @NamedAttributeNode(value = "id"),
        @NamedAttributeNode(value = "owner", subgraph = "Person.details"),
        @NamedAttributeNode(value = "persons", subgraph = "Person.details"),
}, subgraphs = {
        @NamedSubgraph(name = "Person.details", attributeNodes = {
                @NamedAttributeNode(value = "id"),
                @NamedAttributeNode(value = "name"),
                @NamedAttributeNode(value = "email")
        })
})
@NamedEntityGraph(name = "Company.addPersonsToNewEvention", attributeNodes = {
        @NamedAttributeNode(value = "id"),
        @NamedAttributeNode(value = "persons", subgraph = "Person.details")
}, subgraphs = {
        @NamedSubgraph(name = "Person.details", attributeNodes = {
                @NamedAttributeNode("id")
        })
})
public class Company {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов.")
    @Column(name = "name")
    private String name;

    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "person_company",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    private Set<Person> persons = new HashSet<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Evention> eventions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @ToString.Exclude
    private Person owner;

    public Company(int companyId) {
        this.id = companyId;
    }

    public Set<Person> getPersons() {
        TreeSet<Person> sortedPersons = new TreeSet<>(Comparator.comparing(Person::getName));
        sortedPersons.addAll(persons);
        return sortedPersons;
    }

    public void addPerson(Person person) {
        this.persons.add(person);
        person.getCompanies().add(this);
    }

    public void removePerson(Person person) {
        this.persons.remove(person);
        person.getCompanies().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return id == company.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
