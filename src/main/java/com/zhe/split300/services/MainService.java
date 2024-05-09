package com.zhe.split300.services;

import com.zhe.split300.models.Evention;
import com.zhe.split300.models.Operation;
import com.zhe.split300.repositories.EventionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class MainService {
    private final EventionRepository eventionRepository;

    @Autowired
    public MainService(EventionRepository eventionRepository) {
        this.eventionRepository = eventionRepository;
    }

    @Transactional
    public void refreshAll() {
        List<Evention> eventions = eventionRepository.findAll();
       for(int i = 0; i < eventions.size(); i ++) {
           Evention evention = eventions.get(i);
           Set<Operation> operationSet = evention.getOperations();

       }
    }
}
