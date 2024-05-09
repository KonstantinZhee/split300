package com.zhe.split300.services;

import com.zhe.split300.models.Calculation;
import com.zhe.split300.models.Evention;
import com.zhe.split300.models.Person;
import com.zhe.split300.models.PersonBalance;
import com.zhe.split300.repositories.CalculationRepository;
import com.zhe.split300.repositories.EventionRepository;
import com.zhe.split300.repositories.PersonRepository;
import com.zhe.split300.services.interfaces.CalculationService;
import com.zhe.split300.services.interfaces.PersonBalanceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional(readOnly = true)
public class CalculationServiceImpl implements CalculationService {
    private final CalculationRepository calculationRepository;
    private final EventionRepository eventionRepository;
    private final PersonRepository personRepository;
    private final PersonBalanceService personBalanceService;

    @Autowired
    public CalculationServiceImpl(CalculationRepository calculationRepository, EventionRepository eventionRepository,
                                  PersonRepository personRepository, PersonBalanceService personBalanceService) {
        this.calculationRepository = calculationRepository;
        this.eventionRepository = eventionRepository;
        this.personRepository = personRepository;
        this.personBalanceService = personBalanceService;
    }


    @Override
    @Transactional
    public Evention createCalculations(UUID eventionId) {
        log.info("createCalculations(UUID eventionId)");
        Evention evention = eventionRepository.findByIdWithAllFields(eventionId)
                .orElseThrow(() -> new EntityNotFoundException("Evention not found"));
        Set<PersonBalance> personBalances = personBalanceService.createNewPersonBalances(evention);
        evention.setPersonBalances(personBalances);
        Set<Calculation> calculations = convertPersonBalancesToCalculations(personBalances, evention);
        evention.setCalculations(calculations); //TODO Очистить Предидущие калькуляции и балансы
        eventionRepository.save(evention);
        personBalanceService.saveNewPersonBalances(personBalances);
        calculationRepository.saveAll(calculations);
        return evention;
    }


    private Set<Calculation> convertPersonBalancesToCalculations(final Set<PersonBalance> personBalances,
                                                                 Evention evention) {
        log.info("convertPersonBalancesToCalculations(final Set<PersonBalance> personBalances, Evention evention)");
        Map<Person, BigDecimal> balancesMap = personBalances.stream().collect(Collectors
                .toMap(PersonBalance::getPerson, PersonBalance::getBalance));
        Set<Calculation> calculations = new TreeSet<>(Comparator
                .comparing(Calculation::getValue).reversed());

        while (true) {
            Person minBalancePerson = findMinBalancePerson(balancesMap);
            Person maxBalancePerson = findMaxBalancePerson(balancesMap);
            if (balancesMap.get(minBalancePerson).compareTo(BigDecimal.ZERO) >= 0 || balancesMap.get(maxBalancePerson)
                    .compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
            BigDecimal calculationValue = balancesMap.get(maxBalancePerson)
                    .min(balancesMap.get(minBalancePerson).abs());
            balancesMap.put(minBalancePerson, balancesMap.get(minBalancePerson).add(calculationValue));
            balancesMap.put(maxBalancePerson, balancesMap.get(maxBalancePerson).subtract(calculationValue));
            calculations.add(new Calculation(evention, maxBalancePerson, minBalancePerson,
                    calculationValue.setScale(2, RoundingMode.CEILING)));
        }
        return calculations;
    }

    private Person findMaxBalancePerson(Map<Person, BigDecimal> balancesMap) {
        return balancesMap.entrySet().stream().max(Map.Entry.comparingByValue())
                .orElseThrow()
                .getKey();
    }

    private Person findMinBalancePerson(Map<Person, BigDecimal> balancesMap) {
        return balancesMap.entrySet().stream().min(Map.Entry.comparingByValue())
                .orElseThrow()
                .getKey();
    }
}
