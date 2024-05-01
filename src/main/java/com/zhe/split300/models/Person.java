package com.zhe.split300.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


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

//    @NotBlank(message = "Имя не должно быть пустым.")
    @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов.")
    @Column(name = "name")
    private String name;

    @Email(message = "Введите корректный адрес электронной почты. Пример: me@gmail.com.")
    @Column(name = "email")
    @Size(max = 50, message = "Адрес электронной почты  должен быть до 50 символов.")
//    @NotBlank(message = "Поле не должно быть пустым.")
    private String email;

    @ToString.Exclude
    @ManyToMany(mappedBy = "persons")
    private List<Company> companies;

    @ToString.Exclude
    @OneToMany(mappedBy = "owner")
    private List<Operation> operations;

    @ToString.Exclude
    @OneToMany(mappedBy = "personPaidFor")
    private List<PaidFor> paidForActions;

    @OneToMany(mappedBy = "owner")
    @ToString.Exclude
    private List<Company> ownedCompanies;

    public Person(int id) {
        setId(id);
    }

}
