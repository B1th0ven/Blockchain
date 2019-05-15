package com.scor.persistance.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scor.persistance.entities.RefCountryEntity;
import com.scor.persistance.repositories.CountryRepository;

@Service
public class CountryService  implements Serializable {

	@Autowired
	private CountryRepository repository;

	public List<RefCountryEntity> getAll() {
		List<RefCountryEntity> list = new ArrayList<RefCountryEntity>();

		for (RefCountryEntity st : repository.findAll()) {
			list.add(st);
		}
		return list;
	}

	public RefCountryEntity findByCode(String code) {
		List<RefCountryEntity> countryId = repository.findByRcCode(code);
		if (countryId != null && !countryId.isEmpty()) {
			return countryId.get(0);
		}
		return null;
	}
}
