package com.zhe.split300.services;

import com.zhe.split300.models.Company;
import com.zhe.split300.models.Evention;
import com.zhe.split300.models.Operation;
import com.zhe.split300.models.Person;
import com.zhe.split300.repositories.CompanyRepository;
import com.zhe.split300.repositories.EventionRepository;
import com.zhe.split300.repositories.OperationRepository;
import com.zhe.split300.repositories.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class EventionService {
    private static final Logger log = LoggerFactory.getLogger(EventionService.class);
    private final EventionRepository eventionRepository;
    private final CompanyRepository companyRepository;
    private final PersonRepository personRepository;

    @Autowired
    public EventionService(EventionRepository eventionRepository, CompanyRepository companyRepository,
                           PersonRepository personRepository, OperationRepository operationRepository) {
        this.eventionRepository = eventionRepository;
        this.companyRepository = companyRepository;
        this.personRepository = personRepository;
    }

    @Transactional
    public void createNewEvention(Evention evention, int companyId) {
        Company company = companyRepository.findOneToAddPersonsToNewEvention(companyId);
       Optional.of(company).ifPresent(c -> {
           evention.setCompany(company);
           evention.setPersons(company.getPersons());
           evention.setStartTime(new Date());
           evention.setBalance(BigDecimal.valueOf(0));
           eventionRepository.save(evention);
        });
    }

    public Set<Evention> findByCompanyId(int companyId) {
        return eventionRepository.findByCompany(new Company(companyId));
    }

    public Evention findOneWithAllFields(UUID id) {
        return eventionRepository.findByIdToMakeCalculations(id);
    }

    public Evention findOneToEdit(UUID id) {
        return eventionRepository.findByIdToEditEvention(id);
    }

    @Transactional
    public void addPersonToEvention(UUID eventionId, Person selectedPerson) {
        eventionRepository.findById(eventionId).ifPresent(evention -> {
            if (evention.getPersons().stream().
                    anyMatch(person -> person.getId() == selectedPerson.getId())) {
                return;
            }
            personRepository.findById(selectedPerson.getId()).ifPresent(evention::addPerson);
        });
    }

    @Transactional
    public void removePersonFromEvention(UUID eventionId, Person selectedPerson) {
        eventionRepository.findById(eventionId).ifPresent(evention -> {
            personRepository.findById(selectedPerson.getId()).ifPresent(person -> {
                evention.removePerson(selectedPerson);
            });
        });
    }

    @Transactional
    public void refreshBalance(UUID eventionId) {
        Evention evention = eventionRepository.findByIdWithOperations(eventionId).orElseThrow(
                () -> new EntityNotFoundException("Evention not found"));
        evention.setBalance(BigDecimal.ZERO);
        for (Operation operation : evention.getOperations()) {
            evention.setBalance(evention.getBalance().add(operation.getValue()));
        }
        eventionRepository.save(evention);
    }

    @Transactional
    public void delete(UUID eventionId) {
        eventionRepository.deleteById(eventionId);
    }

    @Transactional
    public void setNewNameToEvention(Evention evention, UUID eventionId) {
        eventionRepository.updateName(evention.getName(), eventionId);
    }
}
