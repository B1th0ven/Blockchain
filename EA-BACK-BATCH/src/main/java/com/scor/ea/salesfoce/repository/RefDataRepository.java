package com.scor.ea.salesfoce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scor.ea.salesfoce.entities.RefDataEntity;

@Repository
public interface RefDataRepository extends JpaRepository<RefDataEntity, Integer> {
	public List<RefDataEntity> findByTypeAndCodeAndName(String type, String code, String name);
}
