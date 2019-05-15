package com.scor.ea.omega.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scor.ea.omega.entities.CountryEntity;


public interface CountryRepository extends JpaRepository<CountryEntity, Integer>{
	public List<CountryEntity> findByCode(String code);
	public List<CountryEntity> findAll();
}
