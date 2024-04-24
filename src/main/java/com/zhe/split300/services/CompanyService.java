package com.zhe.split300.services;

import com.zhe.split300.models.Company;
import com.zhe.split300.models.Person;
import com.zhe.split300.repositories.CompanyRepository;
import com.zhe.split300.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class CompanyService {

    private static final Logger log = LoggerFactory.getLogger(CompanyService.class);
    CompanyRepository companyRepository;
    PersonRepository personRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, PersonRepository personRepository) {
        this.companyRepository = companyRepository;
        this.personRepository = personRepository;
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Company findOne(int id) {
        return companyRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(Company company) {
        companyRepository.save(company);
    }

    @Transactional
    public void update(int id, Company company) {
        company.setId(id);
        companyRepository.save(company);
    }

    @Transactional
    public void delete(int id) {
        companyRepository.deleteById(id);
    }

    public Optional<Company> findByName(String name) {
        return companyRepository.findByName(name);
    }

    public List<Person> getPersons(int id) {
        return companyRepository.findById(id).map(Company::getPersons).
                orElse(Collections.emptyList());
    }

    @Transactional
    public void addPersonToCompany(int idCompany, Person selectedPerson) {
        companyRepository.findById(idCompany).ifPresent(company -> {
            ArrayList<Person> persons = new ArrayList<>(company.getPersons());
            Optional<Person> personToAdd = Optional.empty();
            for (Person person : persons) {
                if (person.getId() == selectedPerson.getId()) {
                    return;
                }
            }
            personRepository.findById(selectedPerson.getId()).ifPresent(person -> {
                persons.add(person);
                company.setPersons(persons);
            });
        });
    }

    @Transactional
    public void removePersonFromCompany(int idCompany, Person selectedPerson) {
        companyRepository.findById(idCompany).ifPresent(company -> {
            ArrayList<Person> persons = new ArrayList<>(company.getPersons());
            log.info(persons.toString());
            Person personToRemove = null;
            for (Person person : persons) {
                if (person.getId() == selectedPerson.getId()) {
                    personToRemove = person;
                    break;
                }
            }
            if (persons.remove(personToRemove)) {
                company.setPersons(persons);
            }
            log.info(persons.toString());
        });
    }

}
