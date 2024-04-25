package com.zhe.split300.services;

import com.zhe.split300.models.Evention;
import com.zhe.split300.repositories.EventionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EventionService {
    private final EventionRepository eventionRepository;

    @Autowired
    public EventionService(EventionRepository eventionRepository) {
        this.eventionRepository = eventionRepository;
    }

    public List<Evention> findAll() {
        return eventionRepository.findAll();
    }
}
