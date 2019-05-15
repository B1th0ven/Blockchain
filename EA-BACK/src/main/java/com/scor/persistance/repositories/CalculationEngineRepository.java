package com.scor.persistance.repositories;

import org.springframework.data.repository.CrudRepository;

import com.scor.persistance.entities.RefCalculationEngineTypeEntity;

public interface CalculationEngineRepository extends CrudRepository<RefCalculationEngineTypeEntity, Integer>
{

}
