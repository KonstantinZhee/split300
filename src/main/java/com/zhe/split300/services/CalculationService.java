package com.zhe.split300.services;

import com.zhe.split300.models.Calculation;
import com.zhe.split300.models.Evention;
import com.zhe.split300.models.PersonBalance;

import java.util.Set;
import java.util.UUID;

public interface CalculationService {

    Evention createNewCalculations(UUID eventionId);

    void deleteAllByEvention(Evention evention);
    
    Set<Calculation> convertPersonBalancesToCalculations(final Set<PersonBalance> personBalances,
                                                         Evention evention);


    void updateCalculations(Evention evention);
}
