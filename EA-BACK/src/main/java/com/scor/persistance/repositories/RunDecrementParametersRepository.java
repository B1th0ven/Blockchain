package com.scor.persistance.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.scor.persistance.entities.DecrementParametersEntity;
import com.scor.persistance.entities.RunEntity;

public interface RunDecrementParametersRepository extends CrudRepository<DecrementParametersEntity, Integer> {
	List<DecrementParametersEntity> findByRunByDpRunId(RunEntity id);
}
