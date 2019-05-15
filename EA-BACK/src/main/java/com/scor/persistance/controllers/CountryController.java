package com.scor.persistance.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scor.persistance.entities.RefCountryEntity;
import com.scor.persistance.services.CountryService;

import java.util.List;

@RestController
public class CountryController {

    @Autowired
    private CountryService service;

    @RequestMapping("/countries")
    public List<RefCountryEntity> getClients()
    {
        return service.getAll();
    }

}
