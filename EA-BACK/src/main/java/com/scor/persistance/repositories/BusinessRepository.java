package com.scor.persistance.repositories;

import org.springframework.data.repository.CrudRepository;

import com.scor.persistance.entities.RefLobEntity;

public interface BusinessRepository extends CrudRepository<RefLobEntity, Integer>
{

}
