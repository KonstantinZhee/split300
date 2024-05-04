package com.zhe.split300.services;

import com.zhe.split300.models.Operation;
import com.zhe.split300.repositories.EventionRepository;
import com.zhe.split300.repositories.OperationRepository;
import com.zhe.split300.repositories.PersonRepository;
import com.zhe.split300.services.interfaces.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class OperationServiceImpl implements OperationService {
    private final OperationRepository operationRepository;
    private final EventionRepository eventionRepository;
    private final PersonRepository personRepository;

    @Autowired
    public OperationServiceImpl(OperationRepository operationRepository,
                                EventionRepository eventionRepository, PersonRepository personRepository) {
        this.operationRepository = operationRepository;
        this.eventionRepository = eventionRepository;
        this.personRepository = personRepository;
    }


    @Override
    @Transactional
    public void createNewOperation(Operation operation, UUID eventionId, int personId) {
        eventionRepository.findById(eventionId).ifPresent(evention -> {
            operation.setEvention(evention);
            operation.setTime(new Date());
            personRepository.findById(personId).ifPresent(operation::setOwner);
            operationRepository.save(operation);
        });
    }
}
