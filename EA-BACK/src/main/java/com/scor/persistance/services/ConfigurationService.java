package com.scor.persistance.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scor.persistance.entities.ConfigurationEntity;
import com.scor.persistance.repositories.ConfigurationRepository;

@Service
public class ConfigurationService {
	
	@Autowired
	private ConfigurationRepository configurationRepository;

	public ConfigurationEntity getVersion() {
		ConfigurationEntity version = configurationRepository.findAll().iterator().next();
		return version;
	}
	
	private ConfigurationEntity save(ConfigurationEntity configurationEntity) {
		return configurationRepository.save(configurationEntity);
	}
	
	public ConfigurationEntity setGlobalVersion(int globalVersion) {
		ConfigurationEntity version = configurationRepository.findAll().iterator().next();
		version.setGlobalVersion(globalVersion);
		return configurationRepository.save(version);
	}
	
	public ConfigurationEntity setEaVersion(int eaVersion) {
		ConfigurationEntity version = configurationRepository.findAll().iterator().next();
		version.setEaVersion(eaVersion);
		return configurationRepository.save(version);
	}
	
	public ConfigurationEntity setSasVersion(int sasVersion) {
		ConfigurationEntity version = configurationRepository.findAll().iterator().next();
		version.setSasVersion(sasVersion);
		return configurationRepository.save(version);
	}
	
	public ConfigurationEntity incrementGlobalVersion() {
		ConfigurationEntity version = configurationRepository.findAll().iterator().next();
		version.setGlobalVersion(version.getGlobalVersion() + 1);
		return configurationRepository.save(version);
	}
	
	public ConfigurationEntity incrementEaVersion() {
		ConfigurationEntity version = configurationRepository.findAll().iterator().next();
		version.setEaVersion(version.getEaVersion() + 1);
		return configurationRepository.save(version);
	}
	
	public ConfigurationEntity incrementSasVersion() {
		ConfigurationEntity version = configurationRepository.findAll().iterator().next();
		version.setSasVersion(version.getSasVersion() + 1);
		return configurationRepository.save(version);
	}
	
}
