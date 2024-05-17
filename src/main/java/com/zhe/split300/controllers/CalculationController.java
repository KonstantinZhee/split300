package com.zhe.split300.controllers;

import com.zhe.split300.models.Evention;
import com.zhe.split300.services.CalculationService;
import com.zhe.split300.utils.ConverterDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Log4j2
@Controller
@RequestMapping
public class CalculationController {
    private final CalculationService calculationService;
    private final ConverterDTO converterDTO;

    @Autowired
    public CalculationController(CalculationService calculationService, ConverterDTO converterDTO) {
        this.calculationService = calculationService;
        this.converterDTO = converterDTO;
    }

    @PostMapping("/v1/persons/{id}/groups/{idc}/events/{eUID}/calculations")
    //Создание в Событии списка с минимальным количеством переводов
    public String createNewCalculations(Model model,
                         @PathVariable("id") int personId,
                         @PathVariable("idc") int companyId,
                         @PathVariable("eUID") UUID eventionId) {
        log.info("GET /v1/persons/{id}/groups/{idc}/events/{eUID}");
        model.addAttribute("personId", personId);
        model.addAttribute("companyId", companyId);
        model.addAttribute("eventionId", eventionId);
        Evention evention = calculationService.createNewCalculations(eventionId);
        model.addAttribute("evention", evention);
        model.addAttribute("balances", converterDTO.convertToPersonBalancesDTO(evention));
        model.addAttribute("calculations",
                converterDTO.sortCalculations(evention.getCalculations()));
        return "eventions/showOne";
    }
}
