package com.scor.persistance.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.scor.persistance.entities.RefCountryEntity;

public interface CountryRepository extends CrudRepository<RefCountryEntity, Integer> {
	List<RefCountryEntity> findByRcCode(String code);
}
