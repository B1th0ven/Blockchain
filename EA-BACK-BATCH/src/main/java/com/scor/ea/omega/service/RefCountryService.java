package com.scor.ea.omega.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scor.ea.omega.entities.CountryEntity;
import com.scor.ea.omega.repository.CountryRepository;

@Service
public class RefCountryService {

	
	@Autowired
	private CountryRepository countryRepository;

	
	public Map<String, CountryEntity> getAll() {
		Map<String, CountryEntity> refCountryMap = new HashMap<>();
		List<CountryEntity> countries = countryRepository.findAll();
		for (CountryEntity country : countries) {
			refCountryMap.put(country.getCode().toLowerCase().trim(), country);
		}
		return refCountryMap;
	}

	public CountryEntity findCountryByCode(String code, Map<String, CountryEntity> countries) {
		CountryEntity country = countries.get(code.toLowerCase().trim());
		return country;
	}

}
