package com.zhe.split300.controllers;

import com.zhe.split300.models.Person;
import com.zhe.split300.services.PersonService;
import com.zhe.split300.utils.PersonValidator;
import jakarta.validation.Valid;
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

@Controller
@RequestMapping("/v1/persons")
public class PersonController {

    private final PersonService personService;
    private final PersonValidator personValidator;

    @Autowired
    public PersonController(PersonService personService, PersonValidator personValidator) {
        this.personService = personService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("person", personService.findAll());
        return "persons/index";
    }

    @GetMapping("/{id}")
    public String showPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personService.findOne(id));
        return "persons/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "persons/new";
    }


    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "persons/new";
        } else {
            personService.save(person);
        }
        return "redirect:/v1/persons";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personService.findOne(id));
        return "persons/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "persons/edit";
        } else {
            personService.update(id, person);
        }
        return "redirect:/v1/persons";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        personService.delete(id);
        return "redirect:/v1/persons";
    }

    @GetMapping("/find")
    public String showFindPage(@ModelAttribute("person") Person person) {
        return "persons/find";
    }

    @PostMapping("/find")
    public String findPerson(Model model, @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "email", required = false) String email) {
        if (name != null && !name.isBlank()) {
            personService.findByName(name).ifPresent(
                    person -> model.addAttribute("person", person));
        } else if (email != null && !email.isBlank()) {
            personService.findByEmail(email).ifPresent(
                    person -> model.addAttribute("person", person));
        }
        return "persons/showfind";
    }

    @GetMapping("/search")
    public String searchPageView() {
        return "persons/search";
    }

    @PostMapping("/search")
    public String searchByNameOrEmail(Model model, @RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "email", required = false) String email) {
        if (name == null && !email.isBlank()) {
            model.addAttribute("persons", personService.searchingByQuery(email));
        } else if (email == null && !name.isBlank())
            model.addAttribute("persons", personService.searchingByQuery(name));
        return "persons/search";
    }

}
