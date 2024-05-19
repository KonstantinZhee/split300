package com.zhe.split300.services;


import com.zhe.split300.models.Operation;
import com.zhe.split300.models.OperationBalance;
import com.zhe.split300.models.Person;

import java.math.BigDecimal;
import java.util.Set;

public interface OperationBalanceService {
    OperationBalance createNewOperationBalance(Operation operation, BigDecimal value, Person person);

    Set<OperationBalance> createNewOperationBalances(Operation operation);
}
