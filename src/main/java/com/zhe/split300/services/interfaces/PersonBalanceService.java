package com.zhe.split300.services.interfaces;

import com.zhe.split300.models.Evention;
import com.zhe.split300.models.PersonBalance;

import java.util.Set;

public interface PersonBalanceService {

    Set<PersonBalance> createNewPersonBalances(Evention evention);

    void saveNewPersonBalances(Set<PersonBalance> personBalances);
}
