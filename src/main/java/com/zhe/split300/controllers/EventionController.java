package com.zhe.split300.controllers;

import com.zhe.split300.services.EventionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/eventions")
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
}
