package com.scor.persistance.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scor.persistance.entities.RefLobEntity;
import com.scor.persistance.services.BusinessService;
import com.scor.persistance.services.DimensionsService;

import java.util.List;

@RestController
public class DimensionsController {

    @Autowired
    private DimensionsService service;

    @RequestMapping("/dimensions")
    public List<String> getDimensions()
    {
        return service.getAll();
    }
}
