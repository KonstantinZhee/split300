package com.zhe.split300.utils;

import com.zhe.split300.dto.PersonBalanceDTO;
import com.zhe.split300.models.Calculation;
import com.zhe.split300.models.Evention;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ConverterDTO {
    public List<PersonBalanceDTO> convertToPersonBalancesDTO(Evention evention) {
        return evention.getPersonBalances().stream()
                .map(personBalance -> new PersonBalanceDTO(personBalance.getPerson().getName(),
                        personBalance.getBalance()))
                .sorted(Comparator.comparing(PersonBalanceDTO::getBalance)
                        .thenComparing(PersonBalanceDTO::getName)).toList();
    }

    public Set<Calculation> sortCalculations(Collection<Calculation> calculations) {
        return calculations.stream().sorted(Comparator
                .comparing(Calculation::getValue).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
