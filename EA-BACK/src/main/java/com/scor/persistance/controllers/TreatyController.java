package com.scor.persistance.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scor.dataProcessing.refdata.services.InjectRefDataService;
import com.scor.persistance.entities.RefTreatyEntity;
import com.scor.persistance.services.TreatyService;

import java.util.List;

@RestController
public class TreatyController {

	@Autowired
	private TreatyService treatyService;

	@Autowired
	private InjectRefDataService service;

	@RequestMapping("/treaties")
	public List<RefTreatyEntity> getTreaties() {
		return treatyService.getAllTreaties();

	}

	@RequestMapping("/treaties/clients/{id}")
	public List<RefTreatyEntity> getTreatiesByClients(@PathVariable("id") int clientId) {
		return treatyService.getAllTreatiesByClientId(clientId);

	}
	
	@RequestMapping("/treaty")
	public List<RefTreatyEntity> getTreaties(@RequestParam("key") String key) {
		return treatyService.getTreatiesByName(key);

	}

}
