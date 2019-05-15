package com.scor.persistance.repositories;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.scor.persistance.entities.EaFilesEntity;

public interface EaFilesRepository extends CrudRepository<EaFilesEntity, Integer> {
	List<EaFilesEntity> findByEafLink(String path);
	List<EaFilesEntity> findByEafDataDeletionLessThanEqual(Date scheduledDate ) ; 
	List<EaFilesEntity> findByEafDataDeletion(Date scheduledDate ) ; 
	
}
