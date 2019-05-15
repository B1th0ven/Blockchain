package com.scor.persistance.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.scor.persistance.entities.RunCalcEntity;

import java.util.List;

public interface RunCalcRepository extends CrudRepository<RunCalcEntity, Integer> {

    List<RunCalcEntity> findByRclcRunIdOrderByRclcStartDateDesc(int id);

    RunCalcEntity findByRclcId(int id);

    @Transactional
    void deleteAllByRclcRunId(int id);
}
