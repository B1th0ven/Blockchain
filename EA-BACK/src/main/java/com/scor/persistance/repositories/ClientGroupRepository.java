package com.scor.persistance.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.scor.persistance.entities.RefParentGroupEntity;
import com.scor.persistance.entities.RefUltimateGroupEntity;

public interface ClientGroupRepository extends CrudRepository<RefParentGroupEntity, Integer>
{
	public List<RefParentGroupEntity> findByRpgCode(String code);
	
	public List<RefParentGroupEntity> findByUltimateGroup(RefUltimateGroupEntity ugId);
	
	public List<RefParentGroupEntity> findByUltimateGroupNotNull();
	
	public RefParentGroupEntity findByRpgCodeAndRpgDescription(String code,String flag);
	public List<RefParentGroupEntity> findByRpgDescription(String flag);
	public List<RefParentGroupEntity> findByRpgParentId(RefParentGroupEntity ugId);
}
