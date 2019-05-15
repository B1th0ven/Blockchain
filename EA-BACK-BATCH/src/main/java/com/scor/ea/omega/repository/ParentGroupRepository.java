package com.scor.ea.omega.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.scor.ea.omega.entities.ParentGroupEntity;


public interface ParentGroupRepository extends CrudRepository<ParentGroupEntity, Integer> {
	public List<ParentGroupEntity> findByCode(String code);

}
