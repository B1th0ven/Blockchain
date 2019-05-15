package com.scor.persistance.repositories;

import org.springframework.data.repository.CrudRepository;

import com.scor.persistance.entities.RefDataSourceEntity;

public interface DataSourceRepository extends CrudRepository<RefDataSourceEntity, Integer>
{

}
