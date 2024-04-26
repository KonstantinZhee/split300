package com.zhe.split300.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1")
public class MainController {

    @GetMapping
    public String index() {
        return "index";
    }
}
