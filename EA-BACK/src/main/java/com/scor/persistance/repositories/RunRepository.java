package com.scor.persistance.repositories;

import org.springframework.data.repository.CrudRepository;

import com.scor.persistance.entities.RunEntity;

import java.util.List;

public interface RunRepository extends CrudRepository<RunEntity, Integer>
{
    List<RunEntity> findByRunDsId(Integer id);

    List<RunEntity> findByRunStId(Integer id);

    RunEntity findByRunId(Integer id);
    
    List<RunEntity> findAllByRunStatus(String statuts);

}
