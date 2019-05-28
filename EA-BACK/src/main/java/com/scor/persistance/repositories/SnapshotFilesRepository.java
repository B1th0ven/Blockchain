package com.scor.persistance.repositories;

import com.scor.persistance.entities.DataSetEntity;
import com.scor.persistance.entities.SnapshotFilesEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SnapshotFilesRepository extends CrudRepository<SnapshotFilesEntity,Long> {
    List<SnapshotFilesEntity> findByDataSet(int dsId);
}
