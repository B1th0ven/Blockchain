package com.scor.persistance.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.scor.persistance.entities.RefDataEntity;

@Repository
public interface RefDataRepository extends CrudRepository<RefDataEntity, Integer> {

	public RefDataEntity findByTypeAndCodeAndName(String type, String code, String name);
}
