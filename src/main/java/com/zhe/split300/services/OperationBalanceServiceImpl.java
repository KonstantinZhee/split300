package com.zhe.split300.services;


import com.zhe.split300.models.Operation;
import com.zhe.split300.models.OperationBalance;
import com.zhe.split300.models.Person;
import com.zhe.split300.repositories.OperationBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class OperationBalanceServiceImpl implements OperationBalanceService {
    private final OperationBalanceRepository operationBalanceRepository;

    @Autowired
    public OperationBalanceServiceImpl(OperationBalanceRepository operationBalanceRepository) {
        this.operationBalanceRepository = operationBalanceRepository;
    }

    @Override
    @Transactional
    public OperationBalance createNewOperationBalance(Operation operation, BigDecimal value, Person person) {
        OperationBalance operationBalance = new OperationBalance(operation, value, person);
        operationBalanceRepository.save(operationBalance);
        return operationBalance;
    }

    @Override
    @Transactional
    public Set<OperationBalance> createNewOperationBalances(Operation operation) {
        Set<OperationBalance> operationBalances = new HashSet<>();
        Set<Person> persons = operation.getEvention().getPersons();
        Person operationOwner = operation.getOwner();
        BigDecimal operationValue = operation.getValue();
        persons.remove(operationOwner);
        if (!persons.isEmpty()) {
            BigDecimal personsCount = BigDecimal.valueOf(persons.size());
            BigDecimal personsValue = operationValue
                    .divide(personsCount, 4, RoundingMode.CEILING);
            for (Person person : persons) {
                operationBalances.add(createNewOperationBalance(operation, personsValue, person));
            }
            operationBalances
                    .add(createNewOperationBalance(operation, operationValue.negate(), operationOwner));
        }
        return operationBalances;
    }

}
