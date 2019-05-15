package com.scor.ea.omega.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.scor.ea.omega.entities.CedentNameEntity;

@Repository
public interface CedentNameRepository extends CrudRepository<CedentNameEntity, Integer> {
	public List<CedentNameEntity> findAll();
	public List<CedentNameEntity> findByName(String name);
	public List<CedentNameEntity> findByCode(String code);
}
