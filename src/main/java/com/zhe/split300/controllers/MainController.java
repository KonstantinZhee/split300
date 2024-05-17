package com.zhe.split300.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("/v1")
public class MainController {

    @GetMapping
    public String index() {
        log.info("start page");
        return "index";
    }

}
