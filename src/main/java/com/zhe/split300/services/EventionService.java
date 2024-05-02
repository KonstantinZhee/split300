package com.zhe.split300.services;

import com.zhe.split300.models.Company;
import com.zhe.split300.models.Evention;
import com.zhe.split300.models.Person;
import com.zhe.split300.repositories.CompanyRepository;
import com.zhe.split300.repositories.EventionRepository;
import com.zhe.split300.repositories.PersonRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class EventionService {
    private final EventionRepository eventionRepository;
    private final CompanyRepository companyRepository;
    private final PersonRepository personRepository;

    @Autowired
    public EventionService(EventionRepository eventionRepository, CompanyRepository companyRepository, PersonRepository personRepository) {
        this.eventionRepository = eventionRepository;
        this.companyRepository = companyRepository;
        this.personRepository = personRepository;
    }

    public List<Evention> findAll() {
        return eventionRepository.findAll();
    }

    @Transactional
    public void createNewEvention(Evention evention, int companyId) {
        companyRepository.findById(companyId).ifPresent(company -> {
            evention.setCompany(company);
            evention.setStartTime(new Date());
            evention.setBalance(BigDecimal.valueOf(0));
            eventionRepository.save(evention);
        });
    }

    public List<Evention> findByCompanyId(int companyId) {
        return eventionRepository.findByCompany(new Company(companyId));
    }

    public Evention findOneById(UUID eventionId) {
        return eventionRepository.findById(eventionId).orElse(null);
    }

    public Evention findOneWithPersons(UUID id) {
        Optional<Evention> evention = eventionRepository.findById(id);
        evention.ifPresent(event -> {
//            Hibernate.initialize(event.getPersons());
            List<Person> personsEvention = event.getPersons();
            Company company = event.getCompany();
            List<Person> personsCompany = company.getPersons();
        });
        return evention.orElse(null);
    }

    @Transactional
    public void addPersonToEvention(UUID eventionId, Person selectedPerson) {
        eventionRepository.findById(eventionId).ifPresent(evention -> {
            ArrayList<Person> persons = new ArrayList<>(evention.getPersons());
            for (Person person : persons) {
                if (person.getId() == selectedPerson.getId()) {
                    return;
                }
            }
            personRepository.findById(selectedPerson.getId()).ifPresent(person -> {
                persons.add(person);
                evention.setPersons(persons);
            });
        });
    }

    @Transactional
    public void removePersonFromEvention(UUID eventionId, Person selectedPerson) {
        eventionRepository.findById(eventionId).ifPresent(company -> {
            ArrayList<Person> persons = new ArrayList<>(company.getPersons());
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
        });
    }
}
