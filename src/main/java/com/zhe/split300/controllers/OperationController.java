package com.zhe.split300.controllers;

import com.zhe.split300.models.Evention;
import com.zhe.split300.models.Operation;
import com.zhe.split300.services.interfaces.OperationService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Log4j2
@Controller
@RequestMapping
public class OperationController {
    private final OperationService operationService;

    @Autowired
    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    @PostMapping("/v1/persons/{id}/groups/{idc}/events/{eUID}/operations")
    //Создаем одну новую запись Операции из представления
    public String createNewOperation(Model model, @ModelAttribute("operation") @Valid Operation operation,
                         BindingResult bindingResult,
                         @PathVariable("id") int personId,
                         @PathVariable("idc") int companyId,
                         @PathVariable("eUID") UUID eventionId) {
        log.info("POST /v1/persons/{id}/groups/{idc}/events/{eUID}/operations");
        model.addAttribute("personId", personId);
        model.addAttribute("companyId", companyId);
        model.addAttribute("eventionId", eventionId);
        if (bindingResult.hasErrors()) {
            return "operations/new";
        } else {
            operationService.createNewOperation(operation, eventionId, personId);
            model.addAttribute("eventionId", operation.getUid());
        }
        return "/v1/persons/{id}/groups/{idc}/events/{eUID}/operations/{oUID}";
    }
    //TODO: showOne , GET one
}
