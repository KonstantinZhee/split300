package com.zhe.split300.services.interfaces;


import com.zhe.split300.models.Operation;
import com.zhe.split300.models.Person;

import java.math.BigDecimal;

public interface OperationBalanceService {
    void createNewOperationBalance(Operation operation, BigDecimal value, Person person);

    void createNewOperationBalances(Operation operation);
}
