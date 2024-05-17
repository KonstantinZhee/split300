package com.zhe.split300.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
public class PersonBalanceDTO {
    private String name;
    private BigDecimal balance;

    public PersonBalanceDTO(String personName, BigDecimal balance) {
        this.name = personName;
        this.balance = balance.setScale(2, RoundingMode.CEILING);
    }
}
