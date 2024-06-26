package com.zhe.split300.controllers;

import com.zhe.split300.models.Person;
import com.zhe.split300.services.CompanyService;
import com.zhe.split300.services.PersonService;
import com.zhe.split300.utils.PersonValidator;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
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
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@Controller
public class PersonController {

    private final PersonService personService;
    private final PersonValidator personValidator;

    @Autowired
    public PersonController(PersonService personService, PersonValidator personValidator, CompanyService companyService) {
        this.personService = personService;
        this.personValidator = personValidator;
    }

    @GetMapping("/v1/persons")
    public String index(Model model) {
        log.info("GET: /v1/persons");
        model.addAttribute("person", personService.findAll());
        return "persons/index";
    }

    @GetMapping("/v1/persons/{id}")
    public String showPerson(@PathVariable("id") int id, Model model) {
        log.info("GET: /v1/persons/{id}");
        Person person = personService.findOneWithCompanies(id);
        model.addAttribute("person", person);
        model.addAttribute("companies", person.getCompanies());
        model.addAttribute("ownedCompanies", person.getOwnedCompanies());
        return "persons/show";
    }

    @GetMapping("/v1/persons/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        log.info("GET: /v1/persons/new");
        return "persons/new";
    }


    @PostMapping("/v1/persons")
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        log.info("POST: /v1/persons");
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "persons/new";
        } else {
            personService.save(person);
        }
        return "redirect:/v1/persons";
    }

    @GetMapping("/v1/persons/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        log.info("GET: /v1/persons/{id}/edit");
        model.addAttribute("person", personService.findOne(id));
        return "persons/edit";
    }

    @PatchMapping("/v1/persons/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        log.info("PATCH: /v1/persons/{id}");
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "persons/edit";
        } else {
            personService.update(id, person);
        }
        return "redirect:/v1/persons";
    }

    @DeleteMapping("/v1/persons/{id}")
    public String delete(@PathVariable("id") int id) {
        log.info("DELETE: /v1/persons/{id}");
        personService.delete(id);
        return "redirect:/v1/persons";
    }

    @GetMapping("/v1/persons/find")
    public String showFindPage(@ModelAttribute("person") Person person) {
        log.info("GET: /v1/persons/find");
        return "persons/find";
    }

    @PostMapping("/v1/persons/find")
    public String findPerson(Model model, @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "email", required = false) String email) {
        log.info("POST: /v1/persons/find");
        if (name != null && !name.isBlank()) {
            personService.findByName(name).ifPresent(
                    person -> model.addAttribute("person", person));
        } else if (email != null && !email.isBlank()) {
            personService.findByEmail(email).ifPresent(
                    person -> model.addAttribute("person", person));
        }
        return "persons/showfind";
    }

    @GetMapping("/v1/persons/search")
    public String searchPageView() {
        log.info("GET: /v1/persons/search");
        return "persons/search";
    }

    @PostMapping("/v1/persons/search")
    public String searchByNameOrEmail(Model model, @RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "email", required = false) String email) {
        log.info("POST /v1/persons/search");
        if (name == null && !email.isBlank()) {
            model.addAttribute("persons", personService.searchingByQuery(email));
        } else if (email == null && !name.isBlank())
            model.addAttribute("persons", personService.searchingByQuery(name));
        return "persons/search";
    }


}
