package com.zhe.split300.services;

import com.zhe.split300.models.Evention;

import java.util.UUID;

public interface CalculationService {

    Evention createNewCalculations(UUID eventionId);

    void deleteAllByEvention(Evention evention);
}
