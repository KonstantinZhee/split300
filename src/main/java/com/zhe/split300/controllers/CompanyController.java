package com.zhe.split300.controllers;

import com.zhe.split300.models.Company;
import com.zhe.split300.models.Person;
import com.zhe.split300.services.CompanyService;
import com.zhe.split300.services.EventionService;
import com.zhe.split300.services.PersonService;
import com.zhe.split300.utils.CompanyValidator;
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
public class CompanyController {
    private final CompanyService companyService;
    private final CompanyValidator companyValidator;
    private final PersonService personService;

    @Autowired
    public CompanyController(CompanyService companyService, CompanyValidator companyValidator,
                             PersonService personService, EventionService eventionService) {
        this.companyService = companyService;
        this.companyValidator = companyValidator;
        this.personService = personService;
    }

    @GetMapping("/v1/persons/{id}/groups")
    //Просмотр групп в которых есть участник
    public String getCompaniesByPersonId(Model model, @PathVariable("id") int personId) {
        log.info("GET: /v1/persons/{id}/groups");
        // TODO companyService
        Person person = personService.findOneWithCompanies(personId);
        model.addAttribute("companies", person.getCompanies());
        model.addAttribute("person", person);
        model.addAttribute("ownedCompanies", person.getOwnedCompanies());
        return "persons/show";
    }

    @GetMapping("/v1/persons/{id}/groups/{idc}")
    //Выбираем группу в которой состоит участник для дальнейшей работы с ней.
    public String selectPersonsCompanyById(Model model,
                                           @PathVariable("id") int personId,
                                           @PathVariable("idc") int companyId) {
        log.info("GET     /v1/persons/{id}/groups/{idc}");

        Company company = companyService.findOneWithPersonsAndEvention(companyId);
        Person person = personService.findOne(personId);
        if (company.getPersons().contains(person) || company.getOwner().equals(person)) {
            model.addAttribute("personId", personId);
            model.addAttribute("companyId", companyId);
            model.addAttribute("company", company);
            model.addAttribute("eventions", company.getEventions());
            return "groups/showOne";
        }
        return String.format("redirect:/v1/persons/%d/groups", personId);
    }

    @GetMapping("/v1/persons/{id}/groups/new")
    //получаем форму новой записи, где пользователь хозяин группы
    public String newCompany(Model model, @ModelAttribute("company") Company company,
                             @PathVariable("id") int personId) {
        log.info("GET: /v1/persons/{id}/groups/new");
        model.addAttribute("personId", personId);
        return "groups/new";
    }

    @PostMapping("/v1/persons/{id}/groups")
    //создаем новую группу с владельцем
    public String create(Model model, @ModelAttribute("company") @Valid Company company,
                         BindingResult bindingResult, @PathVariable("id") int personId) {
        log.info("POST    /v1/persons/{id}/groups");
        companyValidator.validate(company, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("personId", personId);
            return "groups/new";
        } else {
            company.setOwner(personService.findOne(personId));
            companyService.save(company);
            return String.format("redirect:/v1/persons/%d/groups", personId);
        }
    }

    @GetMapping("/v1/persons/{id}/groups/{idc}/edit")
    //получаем форму составления новой записи, где пользователь хозяин группы
    public String getPageToEditCompany(Model model, @ModelAttribute("company") Company company,
                                       @PathVariable("id") int personId, @PathVariable("idc") int companyId) {
        log.info("GET    /v1/persons/{id}/groups/{idc}/edit");
        if (companyService.isOwner(personId, companyId)) {
            log.info("groups/edit");
            model.addAttribute("personId", personId);
            model.addAttribute("companyId", companyId);
            model.addAttribute("company", companyService.findOneWithPersonsAndOwner(companyId));
            return "groups/edit";
        }
        return String.format("redirect:/v1/persons/%d/groups/%d", personId, companyId);
    }

    @PatchMapping("/v1/persons/{id}/groups/{idc}")
    //принимаем PATCH, чтобы изменить название группы
    public String editCompany(Model model, @ModelAttribute("company") @Valid Company company,
                              @PathVariable("id") int personId, @PathVariable("idc") int companyId,
                              BindingResult bindingResult) {
        log.info("PATCH     /v1/persons/{id}/groups/{idc}");
        model.addAttribute("personId", personId);
        model.addAttribute("companyId", companyId);
        companyValidator.validate(company, bindingResult);
        if (bindingResult.hasErrors()) {
            log.info("groups/edit");
            return "groups/edit";
        } else {
            companyService.update(companyId, personId, company);
        }
        log.info(String.format("redirect:/v1/persons/%d/groups/%d", personId, companyId));
        return String.format("redirect:/v1/persons/%d/groups/%d", personId, companyId);
    }

    @PostMapping("/v1/persons/{id}/groups/{idc}")
    //Поиск всех людей для дальнейшего добавления в группу
    public String searchPerson(Model model, @PathVariable("id") int personId, @PathVariable("idc") int companyId,
                               @RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "email", required = false) String email,
                               @RequestParam(value = "id") int id) {
        log.info("POST    /v1/persons/{id}/groups/{idc}");
        if (name == null && !email.isBlank()) {
            model.addAttribute("personsResponse", personService.searchingByQuery(email));
        } else if (email == null && !name.isBlank()) {
            model.addAttribute("personsResponse", personService.searchingByQuery(name));
        }
        Company company = companyService.findOneWithPersonsAndOwner(companyId);
        model.addAttribute("company", company);
        model.addAttribute("persons", company.getPersons());
        model.addAttribute("personId", personId);
        model.addAttribute("companyId", companyId);
        return "groups/edit";
    }

    @PatchMapping("/v1/persons/{id}/groups/{idc}/add")
    //Добавить человека в группу
    public String assignPersonToCompany(Model model, @PathVariable("id") int personId, @PathVariable("idc") int companyId,
                                        @ModelAttribute("personToAdd") Person person) {
        log.info("PATCH    /v1/persons/{id}/groups/{idc}/add");
        companyService.addPersonToCompany(companyId, person);
        model.addAttribute("personId", personId);
        model.addAttribute("companyId", companyId);
        return String.format("redirect:/v1/persons/%d/groups/%d/edit", personId, companyId);
    }

    @PatchMapping("/v1/persons/{id}/groups/{idc}/remove")
    //Удалить человека из группы.
    public String removePersonFromCompany(@PathVariable("id") int personId,
                                          @PathVariable("idc") int companyId,
                                          @ModelAttribute("personToRemoveId") Person person) {
        log.info("PATCH     /v1/persons/{id}/groups/{idc}/remove");
        companyService.removePersonFromCompany(companyId, person);
        return String.format("redirect:/v1/persons/%d/groups/%d/edit", personId, companyId);
    }


    @DeleteMapping("/v1/persons/{id}/groups/{idc}")
    public String delete(Model model,
                         @PathVariable("id") int personId,
                         @PathVariable("idc") int companyId) {
        log.info("DELETE /v1/persons/{id}/groups/{idc}");
        companyService.delete(companyId);
        return "redirect:/v1/persons/{id}/groups";
    }

}
