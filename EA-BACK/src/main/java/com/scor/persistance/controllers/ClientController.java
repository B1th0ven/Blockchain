package com.scor.persistance.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scor.persistance.entities.RefCedentNameEntity;
import com.scor.persistance.services.ClientService;

import java.util.List;
import java.util.Optional;

@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;

	@RequestMapping("/clients")
	public Page<RefCedentNameEntity> getClients(@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size,@RequestParam("searchCriteria") Optional<String> searchCriteria ) {
     	return clientService.findAll(page, size , searchCriteria);
	}

    @RequestMapping("/clients/groups/{id}")
    public List<RefCedentNameEntity> getClientsByParentGroup(@PathVariable("id") int parentId)
    {
        return clientService.getClientsByParentGroup(parentId);
    }
    
    @RequestMapping("/client/{id}")
    public RefCedentNameEntity getClientById(@PathVariable("id") int id)
    {
        return clientService.getClientById(id);
    }

    @RequestMapping("/updateClients")
    public List<RefCedentNameEntity> updateClients()
    {
        return clientService.getAll();
    }
}
