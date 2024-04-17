package com.zhe.split300.services;

import com.zhe.split300.models.Company;
import com.zhe.split300.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class CompanyService {

    CompanyRepository companyRepository;
    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }
    public Company findOne(int id) {
        return companyRepository.findById(id).orElse(null);
    }
}
