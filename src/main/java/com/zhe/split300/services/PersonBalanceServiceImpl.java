package com.zhe.split300.services;

import com.zhe.split300.models.Calculation;
import com.zhe.split300.models.Evention;
import com.zhe.split300.models.Operation;
import com.zhe.split300.models.OperationBalance;
import com.zhe.split300.models.Person;
import com.zhe.split300.models.PersonBalance;
import com.zhe.split300.repositories.PersonBalanceRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Log4j2
@Service
@Transactional(readOnly = true)
public class PersonBalanceServiceImpl implements PersonBalanceService {
    private final PersonBalanceRepository personBalanceRepository;

    @Autowired
    public PersonBalanceServiceImpl(PersonBalanceRepository personBalanceRepository) {
        this.personBalanceRepository = personBalanceRepository;
    }

    private Map<Person, BigDecimal> getPersonBigDecimalMap(Set<Operation> operations) {
        log.info("getPersonBigDecimalMap (Set<Operation> operations)");
        Map<Person, BigDecimal> balances = new HashMap<>();
        for (Operation operation : operations) {
            Set<OperationBalance> operationBalances = operation.getOperationBalances();
            for (OperationBalance operationBalance : operationBalances) {
                Person person = operationBalance.getPerson();
                BigDecimal balance = operationBalance.getBalance();
                if (!balances.containsKey(person)) {
                    balances.put(person, new BigDecimal(0));
                }
                balances.put(person, balances.get(person).add(balance));
            }
        }
        return balances;
    }

    @Transactional
    public Set<PersonBalance> createNewPersonBalances(Evention evention) {
        log.info("createNewPersonBalances(Evention evention)");
        Set<Operation> operations = evention.getOperations();
        Map<Person, BigDecimal> balances = getPersonBigDecimalMap(operations);
        Set<PersonBalance> personBalances = new HashSet<>();
        for (Map.Entry<Person, BigDecimal> entry : balances.entrySet()) {
            personBalances.add(new PersonBalance(evention, entry.getValue(), entry.getKey()));
        }
        evention.setPersonBalances(personBalances);
        personBalanceRepository.saveAll(personBalances);
        return personBalances;
    }

    @Override
    @Transactional
    public void deleteAllByEvention(Evention evention) {
        log.info("deleteAllByEvention(Evention evention)");
        personBalanceRepository.deleteAll(evention.getPersonBalances());
        evention.getPersonBalances().clear();
    }

    @Override
    public Set<PersonBalance> updatePersonBalances(Evention evention, Operation operation) {
        Set<OperationBalance> operationBalances = operation.getOperationBalances();
        Set<PersonBalance> personBalances = evention.getPersonBalances();
        Map<Person, BigDecimal> newOperationBalances = new HashMap<>();
        for (PersonBalance personBalance : personBalances) {
            Person person = personBalance.getPerson();
            BigDecimal currentBalance = newOperationBalances.getOrDefault(person, BigDecimal.ZERO);
            newOperationBalances.put(person, currentBalance.add(personBalance.getBalance()));
        }
        for (OperationBalance operationBalance : operationBalances) {
            Person person = operationBalance.getPerson();
            BigDecimal currentBalance = newOperationBalances.getOrDefault(person, BigDecimal.ZERO);
            newOperationBalances.put(person, currentBalance.add(operationBalance.getBalance()));
        }
        for (PersonBalance personBalance : personBalances) {
            Person person = personBalance.getPerson();
            BigDecimal newBalance = newOperationBalances.getOrDefault(person, BigDecimal.ZERO);
            personBalance.setBalance(newBalance);
        }
        return personBalances;
    }

    @Override
    public Set<PersonBalance> updatePersonBalancesTransferringCalculation(Calculation calculation) {
        Set<PersonBalance> personBalances = calculation.getEvention().getPersonBalances();
        for(PersonBalance personBalance : personBalances) {
            if(personBalance.getPerson().equals(calculation.getFromPerson())) {
                personBalance.setBalance(personBalance.getBalance().subtract(calculation.getValue()));
                continue;
            }
            if(personBalance.getPerson().equals(calculation.getToPerson())){
                personBalance.setBalance(personBalance.getBalance().add(calculation.getValue()));
            }
        }
        return personBalances;
    }
}
