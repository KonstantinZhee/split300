package com.zhe.split300.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "Evention")
@ToString
@Builder
public class Evention {

    @Id
    @Column(name = "uid")
    @GeneratedValue
    @UuidGenerator
    private UUID uid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @ToString.Exclude
    private Company company;

    @NotBlank(message = "Имя не должно быть пустым.")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов.")
    @Column(name = "name")
    private String name;

    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MMMM/yyyy - HH:mm:ss")
    private Date startTime;

    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MMMM/yyyy - HH:mm:ss")
    private Date endTime;

    @Column(name = "balance")
    private BigDecimal balance;

    @OneToMany(mappedBy = "evention")
    @ToString.Exclude
    private Set<Operation> operations = new HashSet<>();

    @OneToMany(mappedBy = "evention")
    @ToString.Exclude
    private Set<PersonBalance> personBalances = new HashSet<>();

    @OneToMany(mappedBy = "evention")
    @ToString.Exclude
    private Set<Calculation> calculations = new HashSet<>();

    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "person_evention",
            joinColumns = @JoinColumn(name = "evention_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    private Set<Person> persons = new HashSet<>();

    public Set<Operation> getOperations() {
        TreeSet<Operation> sortedOperations = new TreeSet<>(Comparator.comparing(Operation::getTime));
        sortedOperations.addAll(operations);
        return sortedOperations;
    }

    public Set<Person> getPersons() {
        TreeSet<Person> sortedPersons = new TreeSet<>(Comparator.comparing(Person::getName));
        sortedPersons.addAll(persons);
        return sortedPersons;
    }

    public void addPerson(Person person) {
        this.persons.add(person);
        person.getEventions().add(this);
    }

    public void removePerson(Person person) {
        this.persons.remove(person);
        person.getEventions().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evention evention = (Evention) o;
        return Objects.equals(uid, evention.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uid);
    }


}
