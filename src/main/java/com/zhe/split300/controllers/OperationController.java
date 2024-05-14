package com.zhe.split300.controllers;

import com.zhe.split300.models.Operation;
import com.zhe.split300.services.OperationService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/v1/persons/{id}/groups/{idc}/events/{eUID}/operations/new")
    //Страница создания новой операции
    public String showNewOperationPage(Model model, @ModelAttribute("operation") Operation operation,
                                       @PathVariable("id") int personId,
                                       @PathVariable("idc") int companyId,
                                       @PathVariable("eUID") UUID eventionId) {
        log.info("GET /v1/persons/{id}/groups/{idc}/events/{eUID}/operations/new");
        model.addAttribute("personId", personId);
        model.addAttribute("companyId", companyId);
        model.addAttribute("eventionId", eventionId);
        return "operations/new";
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
            log.info(bindingResult.getAllErrors());
            return "operations/new";
        } else {
            operationService.createNewOperation(operation, eventionId, personId);
            model.addAttribute("operationId", operation.getUid());
        }
        return "redirect:/v1/persons/{id}/groups/{idc}/events/{eUID}/operations/" + operation.getUid();
    }

    @GetMapping("/v1/persons/{id}/groups/{idc}/events/{eUID}/operations/{oUID}")
    //Вывод страницы с Операцией
    public String showOperationById(Model model,
                                    @PathVariable("id") int personId,
                                    @PathVariable("idc") int companyId,
                                    @PathVariable("eUID") UUID eventionId,
                                    @PathVariable("oUID") UUID operationId) {
        log.info("GET /v1/persons/{id}/groups/{idc}/events/{eUID}/operations/{oUID}");
        model.addAttribute("operation",
                operationService.findOneWithAllFields(eventionId));
        model.addAttribute("personId", personId);
        model.addAttribute("companyId", companyId);
        model.addAttribute("eventionId", eventionId);
        model.addAttribute("operationId", operationId);
        return "operations/showOne";
    }

    @DeleteMapping("/v1/persons/{id}/groups/{idc}/events/{eUID}/operations/{oUID}")
    //Удалить операцию из События
    public String delete(Model model,
                         @PathVariable("id") int personId,
                         @PathVariable("idc") int companyId,
                         @PathVariable("eUID") UUID eventionId,
                         @PathVariable("oUID") UUID operationId) {
        log.info("DELETE /v1/persons/{id}/groups/{idc}/events/{eUID}/operations/{oUID}");
        operationService.delete(operationId);
        model.addAttribute("personId", personId);
        model.addAttribute("companyId", companyId);
        model.addAttribute("eventionId", eventionId);
        model.addAttribute("operationId", operationId);
        return "redirect:/v1/persons/{id}/groups/{idc}/events/{eUID}";
    }

    //TODO Изменить логику показа Эвента
    //TODO Оптимизировать запросы
    //TODO Оптимизировать добавление атрибутов при редиректах
    //TODO Редактирование событий
    //TODO Редактирование операции (изменить заплатившего) при нажатии на операцию
    //TODO Удаление Эвента из представления группы
    //TODO Удалить перевод(выполнить перевод) - обновление баланса того, кто выполнил перевод.
    //TODO Удаление операции Каскадное удаление операций При удаление Эвентов - ПРОВЕРИТЬ!
    //TODO HTML showOne Operation - туда потом отображение PaidFor
}
