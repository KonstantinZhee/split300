package com.zhe.split300.services;

import com.zhe.split300.models.Calculation;
import com.zhe.split300.models.Evention;
import com.zhe.split300.models.Operation;
import com.zhe.split300.models.PersonBalance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public interface PersonBalanceService {

    Set<PersonBalance> createNewPersonBalances(Evention evention);

    @Transactional
    void deleteAllByEvention(Evention evention);

    Set<PersonBalance>  updatePersonBalances(Evention evention, Operation operation);
    Set<PersonBalance>  updatePersonBalancesTransferringCalculation(Calculation calculation);
}
