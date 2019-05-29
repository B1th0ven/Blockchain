package com.scor.dataProcessing.snapshot.Service;

import com.scor.persistance.entities.SnapshotFilesEntity;
import com.scor.persistance.repositories.SnapshotFilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SnapshotPersistence {


    @Autowired
    SnapshotFilesRepository snapshotFilesRepository;





    public void testInsertintoSnapshotTable(List<SnapshotFilesEntity> snapshotFilesEntities){
        List<SnapshotFilesEntity>  oldSnapshotFilesEntity = snapshotFilesRepository.findByDataSet(snapshotFilesEntities.get(0).getDataSet());
        if (oldSnapshotFilesEntity != null){
            snapshotFilesRepository.delete(oldSnapshotFilesEntity);
        }
        snapshotFilesRepository.save(snapshotFilesEntities);


    }


    public List<SnapshotFilesEntity> testSelectSnapshotTable(Integer id) {
        return snapshotFilesRepository.findByDataSet(id);
    }
}
