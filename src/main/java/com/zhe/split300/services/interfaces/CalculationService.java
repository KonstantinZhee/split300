package com.zhe.split300.services.interfaces;

import com.zhe.split300.models.Evention;

import java.util.UUID;

public interface CalculationService {
    Evention createCalculations(UUID eventionId);
    void createCalculation(UUID eventionId);
}
