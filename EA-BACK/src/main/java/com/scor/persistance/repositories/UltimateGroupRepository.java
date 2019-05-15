package com.scor.persistance.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.scor.persistance.entities.RefUltimateGroupEntity;

@Repository
public interface UltimateGroupRepository extends CrudRepository<RefUltimateGroupEntity, Integer> {
	public RefUltimateGroupEntity findByCode(String code);
}
