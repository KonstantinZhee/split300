package com.zhe.split300.services;

import com.zhe.split300.models.Evention;
import com.zhe.split300.models.Operation;
import com.zhe.split300.models.PersonBalance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public interface PersonBalanceService {

    Set<PersonBalance> createNewPersonBalances(Evention evention);

    void saveNewPersonBalances(Set<PersonBalance> personBalances);

    @Transactional
    void deleteAllByEvention(Evention evention);

    Set<PersonBalance>  updatePersonBalances(Evention evention, Operation operation);
}
