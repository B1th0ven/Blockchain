package com.scor.persistance.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.scor.persistance.entities.RefCedentNameEntity;

public interface ClientRepository extends CrudRepository<RefCedentNameEntity, Integer> {

	List<RefCedentNameEntity> findByRcnRpgId(int parentId);
	
	Page<RefCedentNameEntity> findAll(Pageable page);
	@Query(value = "Select c from RefCedentNameEntity c  where UPPER(c.rcnShortName) like CONCAT('%',UPPER(:shortName),'%')")
	Page<RefCedentNameEntity>findByRcnShortName(@Param("shortName") String shortName , Pageable pageable );
	RefCedentNameEntity findByRcnCode(String rcnCode);
	RefCedentNameEntity findByRcnId (Integer rcnId) ; 
}
