package com.zhe.split300.services;

import com.zhe.split300.models.Company;
import com.zhe.split300.models.Evention;
import com.zhe.split300.repositories.CompanyRepository;
import com.zhe.split300.repositories.EventionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class EventionService {
    private final EventionRepository eventionRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public EventionService(EventionRepository eventionRepository, CompanyRepository companyRepository) {
        this.eventionRepository = eventionRepository;
        this.companyRepository = companyRepository;
    }

    public List<Evention> findAll() {
        return eventionRepository.findAll();
    }

    @Transactional
    public void createNewEvention(Evention evention, int companyId) {
        companyRepository.findById(companyId).ifPresent(company -> {
            evention.setCompany(company);
            evention.setStartTime(new Date());
            evention.setBalance(BigDecimal.valueOf(0));
            eventionRepository.save(evention);
        });
    }

    public List<Evention> findByCompanyId(int companyId) {
        return eventionRepository.findByCompany(new Company(companyId));
    }
}
