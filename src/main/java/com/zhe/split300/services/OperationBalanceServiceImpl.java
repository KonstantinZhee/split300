package com.zhe.split300.services;


import com.zhe.split300.models.Operation;
import com.zhe.split300.models.OperationBalance;
import com.zhe.split300.models.Person;
import com.zhe.split300.repositories.OperationBalanceRepository;
import com.zhe.split300.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class OperationBalanceServiceImpl implements OperationBalanceService {
    private final OperationBalanceRepository operationBalanceRepository;
    private final PersonRepository personRepository;

    @Autowired
    public OperationBalanceServiceImpl(OperationBalanceRepository operationBalanceRepository,
                                       PersonRepository personRepository) {
        this.operationBalanceRepository = operationBalanceRepository;
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public void createNewOperationBalance(Operation operation, BigDecimal value, Person person) {
        operationBalanceRepository.save(new OperationBalance(operation, value, person));
    }

    @Override
    @Transactional
    public void createNewOperationBalances(Operation operation) {
        Set<Person> persons = operation.getEvention().getPersons();
        Person operationOwner = operation.getOwner();
        BigDecimal operationValue = operation.getValue();
        persons.remove(operationOwner);
        if (!persons.isEmpty()) {
            BigDecimal personsCount = BigDecimal.valueOf(persons.size());
            BigDecimal personsValue = operationValue
                    .divide(personsCount, 4, RoundingMode.CEILING);
            for (Person person : persons) {
                createNewOperationBalance(operation, personsValue, person);
            }
            createNewOperationBalance(operation, operationValue.negate(), operationOwner);
        }
    }

}