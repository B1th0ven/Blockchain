package com.scor.persistance.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.scor.persistance.entities.ConfigurationEntity;
import com.scor.persistance.services.ConfigurationService;

@RestController
public class ConfigurationController {
	
	@Autowired
	private ConfigurationService configurationService; 
	
	@GetMapping("/version")
	public ConfigurationEntity getVersion() {
		return configurationService.getVersion();
	}
	
	@GetMapping("/sasVersion")
	public ConfigurationEntity setSasVersion(@RequestParam("version") int sasVersion) {
		return configurationService.setSasVersion(sasVersion);
	}
	
	@GetMapping("/eaVersion")
	public ConfigurationEntity setEaVersion(@RequestParam("version") int eaVersion) {
		return configurationService.setEaVersion(eaVersion);
	}
	
	@GetMapping("/globalVersion")
	public ConfigurationEntity setVersion(@RequestParam("version") int version) {
		return configurationService.setGlobalVersion(version);
	}
	
	@GetMapping("/incrementGlobalVersion")
	public ConfigurationEntity incrementGlobalVersion() {
		return configurationService.incrementGlobalVersion();
	}
	
	@GetMapping("/incrementEaVersion")
	public ConfigurationEntity incrementEaVersion() {
		return configurationService.incrementEaVersion();
	}
	
	@GetMapping("/incrementSasVersion")
	public ConfigurationEntity incrementSasVersion() {
		return configurationService.incrementSasVersion();
	}

}
