package com.scor.persistance.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.scor.persistance.entities.DecrementExpectedTableEntity;
import com.scor.persistance.entities.RefExpectedTableEntity;
import com.scor.persistance.entities.RefLobEntity;

import java.util.List;
import java.util.Optional;

public interface ExpectedTableRepository extends CrudRepository<RefExpectedTableEntity, Integer>
{
    @Query(value = "SELECT t FROM RefExpectedTableEntity t WHERE retCode LIKE CONCAT(:PREFIX,'%')")
    List<RefExpectedTableEntity> findWithSamePrefix(@Param("PREFIX")String Prefix);

    @Query(value = "SELECT t FROM RefExpectedTableEntity t WHERE retCode LIKE CONCAT(:PREFIX,'%') AND retVersion = ( SELECT MAX(retVersion) FROM RefExpectedTableEntity WHERE retCode LIKE CONCAT(:PREFIX,'%') )")
    List<RefExpectedTableEntity> getLatestVersion(@Param("PREFIX")String Prefix);
    //List<DecrementExpectedTableEntity> deleteByRetDpId(int id);
    
    List<RefExpectedTableEntity> findByRetCode(String code);
    
    List<RefExpectedTableEntity> findByRetDecrementAndRetType(String decrement, String type);
}
