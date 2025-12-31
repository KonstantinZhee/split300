package com.zhe.split300.services;

import com.zhe.split300.models.Evention;
import com.zhe.split300.models.Operation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface OperationService {
    void createNewOperation(Operation operation, UUID eventionId, int PersonId);

    Operation findOneWithAllFields(UUID operationId);

    void deleteOperationAndUpdateEvention(UUID operationId, UUID eventionId);

    @Transactional
    void deleteOperation(Evention evention, UUID operationId);
}
