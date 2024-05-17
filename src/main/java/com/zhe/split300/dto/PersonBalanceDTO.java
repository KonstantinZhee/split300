package com.zhe.split300.dto;

import com.zhe.split300.models.Evention;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class PersonBalanceDTO {
    private String name;
    private BigDecimal balance;

    public PersonBalanceDTO(String personName, BigDecimal balance) {
        this.name = personName;
        this.balance = balance.setScale(2, RoundingMode.CEILING);
    }
    public List<PersonBalanceDTO> convertToPersonBalancesDTO(Evention evention) {
        return evention.getPersonBalances().stream()
                .map(personBalance -> new PersonBalanceDTO(personBalance.getPerson().getName(),
                        personBalance.getBalance()))
                .sorted(Comparator.comparing(PersonBalanceDTO::getBalance)).toList();
    }
}
