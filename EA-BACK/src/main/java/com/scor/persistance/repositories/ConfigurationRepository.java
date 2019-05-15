package com.scor.persistance.repositories;

import org.springframework.data.repository.CrudRepository;

import com.scor.persistance.entities.ConfigurationEntity;


public interface ConfigurationRepository extends CrudRepository<ConfigurationEntity, Integer>  {

}
