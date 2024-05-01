package com.zhe.split300.controllers;

import com.zhe.split300.models.Evention;
import com.zhe.split300.services.EventionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class EventionController {
    private final EventionService eventionService;

    @Autowired
    public EventionController(EventionService eventionService) {
        this.eventionService = eventionService;
    }

    @GetMapping("/v1/persons/{id}/groups/{idc}/events")
    public String getAllEventionsByCompanyId(Model model, @PathVariable("id") int personId,
                                             @PathVariable("idc") int companyId) {
        model.addAttribute("evention", eventionService.findByCompanyId(companyId));
        model.addAttribute("personId", personId);
        model.addAttribute("companyId", companyId);
        return "groups/showOne";
    }
    @PostMapping("/v1/persons/{id}/groups/{idc}/events")
    //Создаем одну новую запись события
    public String create(@ModelAttribute("evention") @Valid Evention evention, BindingResult bindingResult,
                         @PathVariable("id") int personId, @PathVariable("idc") int companyId, Model model) {
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
        model.addAttribute("personId", personId);
        model.addAttribute("companyId", companyId);
        return "eventions/new";
    }
}
