package com.zhe.split300.services;

import com.zhe.split300.models.Evention;
import com.zhe.split300.models.Operation;
import com.zhe.split300.models.OperationBalance;
import com.zhe.split300.models.Person;
import com.zhe.split300.repositories.CalculationRepository;
import com.zhe.split300.repositories.EventionRepository;
import com.zhe.split300.repositories.PersonRepository;
import com.zhe.split300.services.interfaces.CalculationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class CalculationServiceImpl implements CalculationService {
    private final CalculationRepository calculationRepository;
    private final EventionRepository eventionRepository;
    private final PersonRepository personRepository;

    @Autowired
    public CalculationServiceImpl(CalculationRepository calculationRepository, EventionRepository eventionRepository,
                                  PersonRepository personRepository) {
        this.calculationRepository = calculationRepository;
        this.eventionRepository = eventionRepository;
        this.personRepository = personRepository;
    }


    @Override
    @Transactional
    public Evention createCalculations(UUID eventionId) {
        Evention evention = eventionRepository.findById(eventionId).
                orElseThrow(() -> new EntityNotFoundException("Evention not found"));
        Set<Operation> operations = evention.getOperations();
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
        for (Map.Entry<Person, BigDecimal> entry : balances.entrySet()) {
            System.out.println(entry.getKey().getName() + ": " + entry.getValue());
            //TODO PersonBalance save!
        }
        return evention;
    }

    @Override
    public void createCalculation(UUID eventionId) {

    }
}
