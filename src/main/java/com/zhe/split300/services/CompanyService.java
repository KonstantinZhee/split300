package com.zhe.split300.services;

import com.zhe.split300.models.Company;
import com.zhe.split300.models.Person;
import com.zhe.split300.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class CompanyService {

    CompanyRepository companyRepository;
    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
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
    public List<Person> getPersons (int id) {
        return companyRepository.findById(id).map(Company::getPersons).
                orElse(Collections.emptyList());
    }
    @Transactional
    public void addPersonToCompany(int idCompany, Person selectedPerson) {
        companyRepository.findById(idCompany).ifPresent(company -> {
            company.getPersons().add(selectedPerson)
            ;});
    }
//    @Transactional
//    public void addPersonToCompany2(int idCompany, int idPerson) {
//        Person person = new Person(idPerson);
//        companyRepository.findById(idCompany).ifPresent(company -> {
//            company.getPersons().add(person)
//            ;});
//    }
 }
