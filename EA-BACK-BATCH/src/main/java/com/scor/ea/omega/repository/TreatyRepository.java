package com.scor.ea.omega.repository;

import com.scor.ea.omega.entities.RefTreatyEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TreatyRepository extends CrudRepository<RefTreatyEntity, Integer>
{

   List<RefTreatyEntity> findByRtName(String name);

}
