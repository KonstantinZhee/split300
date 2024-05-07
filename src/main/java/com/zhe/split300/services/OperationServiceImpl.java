package com.zhe.split300.services;

import com.zhe.split300.models.Evention;
import com.zhe.split300.models.Operation;
import com.zhe.split300.models.Person;
import com.zhe.split300.repositories.EventionRepository;
import com.zhe.split300.repositories.OperationRepository;
import com.zhe.split300.repositories.PersonRepository;
import com.zhe.split300.services.interfaces.OperationBalanceService;
import com.zhe.split300.services.interfaces.OperationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class OperationServiceImpl implements OperationService {
    private final OperationRepository operationRepository;
    private final EventionRepository eventionRepository;
    private final PersonRepository personRepository;
    private final OperationBalanceService operationBalanceService;

    @Autowired
    public OperationServiceImpl(OperationRepository operationRepository, EventionRepository eventionRepository,
                                PersonRepository personRepository, OperationBalanceService operationBalanceService) {
        this.operationRepository = operationRepository;
        this.eventionRepository = eventionRepository;
        this.personRepository = personRepository;
        this.operationBalanceService = operationBalanceService;
    }


    @Override
    @Transactional
    public void createNewOperation(Operation operation, UUID eventionId, int personId) {
        Evention evention = eventionRepository.findById(eventionId).
                orElseThrow(() -> new EntityNotFoundException("Evention not found"));
        evention.setBalance(evention.getBalance().add(operation.getValue()));
        operation.setEvention(evention);
        operation.setTime(new Date());
        Person person = personRepository.findById(personId).
                orElseThrow(() -> new EntityNotFoundException("Person not found"));
        operation.setOwner(person);
        eventionRepository.save(evention);
        operationRepository.save(operation);
        operationBalanceService.createNewOperationBalances(operation);
    }

    @Override
    public Operation findOneWithAllFields(UUID operationId) {
        return operationRepository.findById(operationId).orElse(null);
    }

    @Override
    @Transactional
    public void delete(UUID operationId) {
        operationRepository.deleteById(operationId);
    }
}
