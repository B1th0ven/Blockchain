package com.scor.persistance.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scor.persistance.entities.RefLobEntity;
import com.scor.persistance.services.BusinessService;

import java.util.List;

@RestController
public class BusinessController {

    @Autowired
    private BusinessService service;

    @RequestMapping("/business")
    public List<RefLobEntity> getClients()
    {
        return service.getAll();
    }
}
