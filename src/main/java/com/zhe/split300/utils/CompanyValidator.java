package com.zhe.split300.utils;

import com.zhe.split300.models.Company;
import com.zhe.split300.services.CompanyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Log4j2
@Component
public class CompanyValidator implements Validator {
    private final CompanyService companyService;

    @Autowired
    public CompanyValidator(CompanyService companyService) {
        this.companyService = companyService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Company.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.info("validate");
        Company company = (Company) target;
        if(companyService.findByName(company.getName()).isPresent())
            errors.rejectValue("name", "","Группа с таким именем уже есть.");
    }
}
