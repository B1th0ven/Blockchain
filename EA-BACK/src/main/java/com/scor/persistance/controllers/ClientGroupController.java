package com.scor.persistance.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scor.persistance.entities.RefParentGroupEntity;
import com.scor.persistance.services.ClientGroupService;

import java.util.List;

@RestController
public class ClientGroupController {

    @Autowired
    private ClientGroupService clientService;

    @RequestMapping("/groups")
    public List<RefParentGroupEntity> getGroups()
    {
        //System.out.println("--------------------------------------------");
        return clientService.getAll();
    }
}
