package com.zhe.split300.controllers;

import com.zhe.split300.models.Company;
import com.zhe.split300.models.Person;
import com.zhe.split300.services.CompanyService;
import com.zhe.split300.services.PersonService;
import com.zhe.split300.utils.CompanyValidator;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Log
@Controller
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;
    private final CompanyValidator companyValidator;
    private final PersonService personService;

    @Autowired
    public CompanyController(CompanyService companyService, CompanyValidator companyValidator, PersonService personService) {
        this.companyService = companyService;
        this.companyValidator = companyValidator;
        this.personService = personService;
    }

    @GetMapping()
    //Получаем все записи (1)
    public String index(Model model) {
        log.info("Start method: index");
        model.addAttribute("company", companyService.findAll());
        return "company/index";
    }

    @PostMapping()
    //Создаем одну новую запись (2)
    public String create(@ModelAttribute("company") @Valid Company company, BindingResult bindingResult) {
        log.info("Start method: create");
        companyValidator.validate(company, bindingResult);
        if (bindingResult.hasErrors()) {
            log.info("Нашлись ошибки в модели.");
            return "company/new";
        } else {
            companyService.save(company);
            log.info("Ошибок в модели не было.");
        }
        return "redirect:/company";
    }

    @GetMapping("/new")
    //получаем форму новой записи (3)
    public String newCompany(@ModelAttribute("company") Company company) {
        log.info("Start method: newCompany");
        return "company/new";
    }

    @GetMapping("/{id}/edit")
    //Форма редактирования (4)
    public String edit(Model model, @PathVariable("id") int id) {
        log.info("Start method: showCompany");
        model.addAttribute("company", companyService.findOne(id));
        return "company/edit";
    }

    @GetMapping("/{id}")
    //Читаем одну запись (5)
    public String showCompany(@PathVariable("id") int id, Model model,
                              @ModelAttribute("person") Person person) {
        log.info("Start method: showCompany");
        model.addAttribute("company", companyService.findOne(id));
        List<Person> persons = companyService.getPersons(id);
        if (persons != null) {
            model.addAttribute("persons", persons);
        }
        return "company/show";
    }

    @PatchMapping("/{id}")
    //Обновление одной записи (6)
    public String update(@ModelAttribute("company") @Valid Company company, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        log.info("Start method: update");
        companyValidator.validate(company, bindingResult);
        if (bindingResult.hasErrors()) {
            log.info("Нашлись ошибки в модели.");
            return "company/edit";
        } else {
            log.info("Ошибок в модели не было.");
            companyService.update(id, company);
        }
        return "redirect:/company";
    }

    @DeleteMapping("/{id}")
    //Удаление одной записи (7)
    public String delete(@PathVariable("id") int id) {
        log.info("Start method: delete");
        companyService.delete(id);
        return "redirect:/company";
    }

    @PostMapping("/searchPerson")
    //Поиск всех людей и добавление выбранного
    public String searchPerson(Model model, @RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "email", required = false) String email,
                               @RequestParam(value = "id") int id) {
        if(name == null && !email.isBlank()) {
            model.addAttribute("personsResponse", personService.searchingByQuery(email));
        } else if (email == null && !name.isBlank()) {
            model.addAttribute("personsResponse", personService.searchingByQuery(name));
        } model.addAttribute("company", companyService.findOne(id));
            model.addAttribute("persons", companyService.getPersons(id));
        return "company/show";
    }

    @PatchMapping("/{id}/add")
    public String assignPersonToCompany(@PathVariable("id") int companyId,
                                        @ModelAttribute("personId") Person person) {
        log.info("*****************" + companyId + "\\n person:" + person);
        companyService.addPersonToCompany(companyId, person);
        return "redirect:/company/" + companyId;
    }

    @PatchMapping("/{id}/remove")
    public String removePersonFromCompany(@PathVariable("id") int companyId,
                                        @ModelAttribute("personToRemoveId") Person person) {
        log.info("*****************\n removePersonFromCompany:" + companyId + "\\n person:" + person);
        companyService.removePersonFromCompany(companyId, person);
        return "redirect:/company/" + companyId;
    }
}
