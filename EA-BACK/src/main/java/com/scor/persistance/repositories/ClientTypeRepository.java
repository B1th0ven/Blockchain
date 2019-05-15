package com.scor.persistance.repositories;

import org.springframework.data.repository.CrudRepository;

import com.scor.persistance.entities.RefRequesterEntity;

public interface ClientTypeRepository extends CrudRepository<RefRequesterEntity, Integer>
{

}
