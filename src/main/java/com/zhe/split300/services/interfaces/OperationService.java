package com.zhe.split300.services.interfaces;

import com.zhe.split300.models.Operation;

import java.util.UUID;

public interface OperationService {
    void createNewOperation(Operation operation, UUID eventionId, int PersonId);

    Operation findOneWithAllFields(UUID operationId);

    void delete(UUID operationId);
}
