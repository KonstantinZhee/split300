package com.zhe.split300.services;

import com.zhe.split300.models.Person;
import com.zhe.split300.repositories.PersonRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log
@Service
@Transactional(readOnly = true)
public class PersonService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findOne(int id) {
        return personRepository.findById(id).orElse(null);
    }

    public Optional<Person> findByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    public Optional<Person> findByName(String name) {
        return personRepository.findByName(name);
    }

    public List<Person> searchingByQuery(String query) {
        if (query.contains("@")) {
            return Optional.of(personRepository.findByEmailIgnoreCaseStartingWith(query)).
                    orElse(Collections.emptyList());
        } else {
            return Optional.of(personRepository.findByNameIgnoreCaseStartingWith(query)).
                    orElse(Collections.emptyList());
        }
    }

    @Transactional
    public void save(Person person) {
        personRepository.save(person);
    }

    @Transactional
    public void update(int id, Person person) {
        person.setId(id);
        personRepository.save(person);
    }

    @Transactional
    public void delete(int id) {
        personRepository.deleteById(id);
    }
}
