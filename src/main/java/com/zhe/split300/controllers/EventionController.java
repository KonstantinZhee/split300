package com.zhe.split300.controllers;

import com.zhe.split300.models.Evention;
import com.zhe.split300.models.Person;
import com.zhe.split300.services.CompanyService;
import com.zhe.split300.services.EventionService;
import com.zhe.split300.utils.ConverterDTO;
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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Log4j2
@Controller
@RequestMapping
public class EventionController {
    private final EventionService eventionService;
    private final CompanyService companyService;
    private final ConverterDTO converterDTO;

    @Autowired
    public EventionController(EventionService eventionService, CompanyService companyService, ConverterDTO converterDTO) {
        this.eventionService = eventionService;
        this.companyService = companyService;
        this.converterDTO = converterDTO;
    }

    @GetMapping("/v1/persons/{id}/groups/{idc}/events")
    //Вывод событий в группе
    public String getAllEventionsByCompanyId(Model model, @PathVariable("id") int personId,
                                             @PathVariable("idc") int companyId) {
        log.info("GET /v1/persons/{id}/groups/{idc}/events");
        model.addAttribute("evention", eventionService.findByCompanyId(companyId));
        model.addAttribute("personId", personId);
        model.addAttribute("companyId", companyId);
        return "groups/showOne";
    }

    @PostMapping("/v1/persons/{id}/groups/{idc}/events")
    //Создаем одну новую запись события
    public String create(@ModelAttribute("evention") @Valid Evention evention,
                         BindingResult bindingResult,
                         @PathVariable("id") int personId,
                         @PathVariable("idc") int companyId, Model model) {
        log.info("POST /v1/persons/{id}/groups/{idc}/events");
        model.addAttribute("personId", personId);
        model.addAttribute("companyId", companyId);
        if (bindingResult.hasErrors()) {
            return "eventions/new";
        } else {
            eventionService.createNewEvention(evention, companyId);
            model.addAttribute("eventionUID", evention.getUid());
        }
        return "redirect:/v1/persons/{id}/groups/{idc}";
    }

    @GetMapping("/v1/persons/{id}/groups/{idc}/events/new")
    public String setNewEvention(Model model, @ModelAttribute("evention") Evention evention,
                                 @PathVariable("id") int personId,
                                 @PathVariable("idc") int companyId) {
        log.info("GET /v1/persons/{id}/groups/{idc}/events/new");
        model.addAttribute("personId", personId);
        model.addAttribute("companyId", companyId);
        return "eventions/new";
    }

    @GetMapping("/v1/persons/{id}/groups/{idc}/events/{eUID}")
    //Вывод страницы с событием
    public String showEventionByEventionId(Model model,
                                           @PathVariable("id") int personId,
                                           @PathVariable("idc") int companyId,
                                           @PathVariable("eUID") UUID eventionId) {
        log.info("GET /v1/persons/{id}/groups/{idc}/events/{eUID}");
        Evention evention = eventionService.findOneWithAllFields(eventionId);
        model.addAttribute("evention", evention);
        model.addAttribute("balances", converterDTO.convertToPersonBalancesDTO(evention));
        model.addAttribute("calculations", converterDTO
                .sortCalculations(evention.getCalculations()));
        model.addAttribute("personId", personId);
        model.addAttribute("companyId", companyId);
        model.addAttribute("eventionId", eventionId);
        return "eventions/showOne";
    }

    @GetMapping("/v1/persons/{id}/groups/{idc}/events/{eUID}/edit")
    //Редактирование события
    public String editEvention(Model model,
                               @PathVariable("id") int personId,
                               @PathVariable("idc") int companyId,
                               @PathVariable("eUID") UUID eventionId) {
        log.info("GET  /v1/persons/{id}/groups/{idc}/events/{eUID}/edit");
        Evention evention = eventionService.findOneWithAllFields(eventionId);
        model.addAttribute("evention", evention);
        model.addAttribute("balances", converterDTO.convertToPersonBalancesDTO(evention));
        model.addAttribute("company", evention.getCompany());
        model.addAttribute("personId", personId);
        model.addAttribute("companyId", companyId);
        model.addAttribute("eventionId", eventionId);
        return "eventions/edit";
    }

    @PatchMapping("/v1/persons/{id}/groups/{idc}/events/{eUID}/addPerson")
    //Добавить человека в событие
    public String assignPersonToEvention(Model model, @ModelAttribute("personToAdd") Person person,
                                         @PathVariable("id") int personId,
                                         @PathVariable("idc") int companyId,
                                         @PathVariable("eUID") UUID eventionId) {
        log.info("PATCH   /v1/persons/{id}/groups/{idc}/events/{eUID}/addPerson");
        model.addAttribute("personId", personId);
        model.addAttribute("companyId", companyId);
        model.addAttribute("eventionId", eventionId);
        eventionService.addPersonToEvention(eventionId, person);
        return String.format("redirect:/v1/persons/%d/groups/%d/events/%s/edit", personId, companyId, eventionId);
    }

    @PatchMapping("/v1/persons/{id}/groups/{idc}/events/{eUID}/removePerson")
    //Удалить человека из события
    public String removePersonFromEvention(Model model, @ModelAttribute("personToRemove") Person person,
                                           @PathVariable("id") int personId,
                                           @PathVariable("idc") int companyId,
                                           @PathVariable("eUID") UUID eventionId) {
        log.info("PATCH   /v1/persons/{id}/groups/{idc}/events/{eUID}/removePerson");
        eventionService.removePersonFromEvention(eventionId, person);
        return String.format("redirect:/v1/persons/%d/groups/%d/events/%s/edit", personId, companyId, eventionId);
    }

    @PatchMapping("/v1/persons/{id}/groups/{idc}/events/{eUID}/refresh")
    //Обновление баланса
    public String refreshOperations(Model model,
                                    @PathVariable("id") int personId,
                                    @PathVariable("idc") int companyId,
                                    @PathVariable("eUID") UUID eventionId) {
        log.info("PATCH   /v1/persons/{id}/groups/{idc}/events/{eUID}/refresh");
        eventionService.refreshBalance(eventionId);
        return String.format("redirect:/v1/persons/%d/groups/%d/events/%s", personId, companyId, eventionId);
    }

    @DeleteMapping("/v1/persons/{id}/groups/{idc}/events/{eUID}")
    //Удалить Событие
    public String delete(Model model,
                         @PathVariable("id") int personId,
                         @PathVariable("idc") int companyId,
                         @PathVariable("eUID") UUID eventionId) {
        log.info("DELETE /v1/persons/{id}/groups/{idc}/events/{eUID}/operations/{oUID}");
        eventionService.delete(eventionId);
        model.addAttribute("personId", personId);
        model.addAttribute("companyId", companyId);
        model.addAttribute("eventionId", eventionId);
        return "redirect:/v1/persons/{id}/groups/{idc}";
    }

}
