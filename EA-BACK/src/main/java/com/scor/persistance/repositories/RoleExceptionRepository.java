package com.scor.persistance.repositories;

import org.springframework.data.repository.CrudRepository;

import com.scor.persistance.entities.RefUserEntity;
import com.scor.persistance.entities.RoleExceptionEntity;
import com.scor.persistance.entities.StudyEntity;

import java.util.List;

public interface RoleExceptionRepository extends CrudRepository<RoleExceptionEntity, Integer> {
    RoleExceptionEntity findByUreRuIdAndUreStId(RefUserEntity ureruid, StudyEntity urestid);
    List<RoleExceptionEntity> findByUreStId(StudyEntity urestid);
    List<RoleExceptionEntity> findByUreRuId(RefUserEntity urestid);
  
}
