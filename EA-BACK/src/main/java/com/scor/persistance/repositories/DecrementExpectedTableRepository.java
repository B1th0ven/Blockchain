package com.scor.persistance.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.scor.persistance.entities.DecrementExpectedTableEntity;

public interface DecrementExpectedTableRepository extends CrudRepository<DecrementExpectedTableEntity, Integer> {

	List<DecrementExpectedTableEntity> findByRetBase(int id);
	List<DecrementExpectedTableEntity> findByRetTrend(int id);
	List<DecrementExpectedTableEntity> findByRetAdjustment(int id);
	
}
