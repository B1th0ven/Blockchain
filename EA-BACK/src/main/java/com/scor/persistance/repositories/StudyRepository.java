package com.scor.persistance.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.scor.persistance.entities.StudyEntity;

import java.io.Serializable;
import java.util.Optional;

public interface StudyRepository extends PagingAndSortingRepository<StudyEntity, Integer>,Serializable
{
    @Query(value = "SELECT MAX(stCode) FROM StudyEntity WHERE stCode LIKE CONCAT(:PREFIX,'%')")
    public Optional<String> findLatestIdPrefix(@Param("PREFIX")String Prefix);

}
