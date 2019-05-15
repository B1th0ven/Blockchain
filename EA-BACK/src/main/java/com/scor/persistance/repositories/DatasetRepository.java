package com.scor.persistance.repositories;

import org.springframework.data.repository.CrudRepository;

import com.scor.persistance.entities.DataSetEntity;
import com.scor.persistance.entities.EaFilesEntity;

import java.util.List;

public interface DatasetRepository extends CrudRepository<DataSetEntity, Integer>
{
    List<DataSetEntity> findByDsStId(int id);
    List<DataSetEntity> findByDsEventExposureFile(EaFilesEntity file);
    List<DataSetEntity> findByDsEventExposureFileOrDsProductFile(EaFilesEntity file,EaFilesEntity file2);

}
