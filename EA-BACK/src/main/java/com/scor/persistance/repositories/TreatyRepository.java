package com.scor.persistance.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.scor.persistance.entities.RefTreatyEntity;

public interface TreatyRepository extends CrudRepository<RefTreatyEntity, Integer>
{

    List<RefTreatyEntity> findByRtRcnId(int id);
    
    @Query("Select c from RefTreatyEntity c where c.rtName like :key%")
    List<RefTreatyEntity> findByRtNameContaining(@Param("key") String key);

}
