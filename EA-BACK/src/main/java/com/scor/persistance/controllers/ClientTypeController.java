package com.scor.persistance.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.scor.persistance.entities.RefRequesterEntity;
import com.scor.persistance.services.ClientTypeService;

import java.util.List;

@RestController
public class ClientTypeController {

    @Autowired
    private ClientTypeService clientService;

    @RequestMapping("/clienttypes")
    public List<RefRequesterEntity> getStudies()
    {
        return clientService.getAllStudies();
    }

    @RequestMapping("/clienttypes/{id}")
    public RefRequesterEntity getClient(@PathVariable("id") String id)
    {
        return clientService.getClient( Integer.parseInt(id) );
    }

    @RequestMapping(method = RequestMethod.POST , value = "/clienttypes" )
    public void postClient(@RequestBody RefRequesterEntity st)
    {
        clientService.postClient(st);
    }
}
