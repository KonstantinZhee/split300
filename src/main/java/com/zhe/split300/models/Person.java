package com.zhe.split300.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "Person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Имя не должно быть пустым.")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов.")
    @Column(name = "name")
    private String name;

    @Email(message = "Введите корректный адрес электронной почты. Пример: me@gmail.com.")
    @Column(name = "email")
    @Size(max = 50, message = "Адрес электронной почты  должен быть до 50 символов.")
    @NotBlank(message = "Поле не должно быть пустым.")
    private String email;

    @ToString.Exclude
    @ManyToMany(mappedBy = "persons")
    private Set<Company> companies;

    @ToString.Exclude
    @OneToMany(mappedBy = "owner")
    private Set<Operation> operations;

    @ToString.Exclude
    @OneToMany(mappedBy = "personPaidFor")
    private Set<PaidFor> paidForActions;

    @OneToMany(mappedBy = "owner")
    @ToString.Exclude
    private Set<Company> ownedCompanies;

    @ToString.Exclude
    @ManyToMany(mappedBy = "persons", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<Company> eventions;

    public Person(int id) {
        setId(id);
    }

}
