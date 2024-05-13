package com.zhe.split300.utils;

import com.zhe.split300.dto.PersonBalanceDTO;
import com.zhe.split300.models.Evention;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class ConverterDTO {
    public List<PersonBalanceDTO> convertToPersonBalancesDTO(Evention evention) {
        return evention.getPersonBalances().stream()
                .map(personBalance -> new PersonBalanceDTO(personBalance.getPerson().getName(),
                        personBalance.getBalance()))
                .sorted(Comparator.comparing(PersonBalanceDTO::getBalance)
                        .thenComparing(PersonBalanceDTO::getName)).toList();
    }
}
