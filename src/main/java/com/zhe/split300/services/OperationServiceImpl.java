package com.zhe.split300.services;

import com.zhe.split300.models.Evention;
import com.zhe.split300.models.Operation;
import com.zhe.split300.models.OperationBalance;
import com.zhe.split300.models.Person;
import com.zhe.split300.models.PersonBalance;
import com.zhe.split300.repositories.EventionRepository;
import com.zhe.split300.repositories.OperationRepository;
import com.zhe.split300.repositories.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class OperationServiceImpl implements OperationService {
    private static final Logger log = LoggerFactory.getLogger(OperationServiceImpl.class);
    private final OperationRepository operationRepository;
    private final EventionRepository eventionRepository;
    private final PersonRepository personRepository;
    private final OperationBalanceService operationBalanceService;
    private final PersonBalanceService personBalanceService;

    @Autowired
    public OperationServiceImpl(OperationRepository operationRepository, EventionRepository eventionRepository,
                                PersonRepository personRepository, OperationBalanceService operationBalanceService,
                                PersonBalanceService personBalanceService) {
        this.operationRepository = operationRepository;
        this.eventionRepository = eventionRepository;
        this.personRepository = personRepository;
        this.operationBalanceService = operationBalanceService;
        this.personBalanceService = personBalanceService;
    }

    @Override
    @Transactional
    public void createNewOperation(Operation operation, UUID eventionId, int personId) {
        Evention evention = eventionRepository.findByIdToAddBalances(eventionId);
        evention.setBalance(evention.getBalance().add(operation.getValue()));
        enrichOperation(evention, operation, personId);
        evention.getOperations().add(operation);
        if (evention.getPersonBalances() == null || evention.getPersonBalances().isEmpty()) {
            Set<PersonBalance> personBalances = personBalanceService.createNewPersonBalances(evention);
            evention.setPersonBalances(personBalances);
        } else {
            evention.setPersonBalances(personBalanceService.updatePersonBalances(evention, operation));
        }
        eventionRepository.save(evention);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private Operation enrichOperation(Evention evention, Operation operation, int personId) {
        operation.setEvention(evention);
        operation.setTime(new Date());
        Person person = personRepository.findById(personId).
                orElseThrow(() -> new EntityNotFoundException("Person not found"));
        operation.setOwner(person);
        operationRepository.save(operation);
        Set<OperationBalance> operationBalances = operationBalanceService.createNewOperationBalances(operation);
        operation.setOperationBalances(operationBalances);
        return operation;
    }

    @Override
    @Transactional
    public Operation findOneWithAllFields(UUID operationId) {
        log.info("findOneWithAllFields");
        return operationRepository.findOneWithDetails(operationId);
    }

    @Override
    @Transactional
    public void delete(UUID operationId) {
        operationRepository.deleteById(operationId);

    }
}
