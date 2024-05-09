package com.zhe.split300.controllers;

import com.zhe.split300.services.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1")
public class MainController {
    private final MainService mainService;

    @Autowired
    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping
    public String index() {
        return "index";
    }
    @GetMapping("/v1/refresh")
    public String refreshAll() {
        mainService.refreshAll();
        return "index";
    }
}
