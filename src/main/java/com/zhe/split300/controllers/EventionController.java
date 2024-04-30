package com.zhe.split300.controllers;

import com.zhe.split300.models.Company;
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

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("evention", eventionService.findAll());
        return "eventions/index";
    }
    @PostMapping()
    //Создаем одну новую запись (2)
    public String create(@ModelAttribute("evention") @Valid Evention evention, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "eventions/new";
        } else {
            eventionService.save(evention);
        }
        return "redirect:/eventions";
    }
    @GetMapping("/v1/persons/{id}/groups/{idc}/events/new")
    public String setNewEvention(@ModelAttribute("evention") Evention evention, @PathVariable("id") int personId,
                                 @PathVariable("idc") int companyId) {

        return "";
    }
}
