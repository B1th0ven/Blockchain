package com.scor.persistance.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scor.persistance.entities.RefCalculationEngineTypeEntity;
import com.scor.persistance.services.CalculationEngineService;

import java.util.List;

@RestController
public class CalculationEngineController {

    @Autowired
    private CalculationEngineService service;

    @RequestMapping("/engines")
    public List<RefCalculationEngineTypeEntity> getClients()
    {
        return service.getAll();
    }
}
