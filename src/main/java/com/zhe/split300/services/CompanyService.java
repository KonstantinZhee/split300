package com.zhe.split300.services;

import com.zhe.split300.models.Company;
import com.zhe.split300.models.Evention;
import com.zhe.split300.models.Person;
import com.zhe.split300.repositories.CompanyRepository;
import com.zhe.split300.repositories.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


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

    public boolean isOwner(int personId, int companyId) {
        Optional<Company> optionalCompany = companyRepository.findById(companyId);
        AtomicInteger ownerId = new AtomicInteger();
        optionalCompany.flatMap(company -> Optional.ofNullable(company.getOwner())).
                ifPresent(person -> ownerId.set(person.getId()));
        return ownerId.get() == personId;
    }

    @Transactional
    public void save(Company company) {
        companyRepository.save(company);
    }

    @Transactional
    public void update(int companyId, int personId, Company company) {
        Company companyToSave = companyRepository.findById(companyId).orElse(null);
        assert companyToSave != null;
        companyToSave.setName(company.getName());
        companyRepository.save(companyToSave);
    }

    @Transactional
    public void delete(int id) {
        companyRepository.deleteById(id);
    }

    public Optional<Company> findByName(String name) {
        return companyRepository.findByName(name);
    }

    public Set<Person> getPersons(int id) {
        return companyRepository.findById(id).map(Company::getPersons).orElse(Collections.emptySet());
    }

    @Transactional
    public void addPersonToCompany(int idCompany, Person selectedPerson) {
        companyRepository.findById(idCompany).ifPresent(company -> {
            personRepository.findById(selectedPerson.getId()).ifPresent(person -> {
                company.addPerson(person);
                companyRepository.save(company);
            });
        });
    }

    @Transactional
    public void removePersonFromCompany(int idCompany, Person selectedPerson) {
       Company company = companyRepository.findOneWithPersonsAndOwner(idCompany);
       company.removePerson(selectedPerson);
       companyRepository.save(company);
    }

    public Company findOneWithPersonsAndEvention(int companyId) {
        return companyRepository.findOneWithPersonsAndEvention(companyId);
    }

    public Company findOneWithPersonsAndOwner(int companyId) {
        return companyRepository.findOneWithPersonsAndOwner(companyId);
    }
}
