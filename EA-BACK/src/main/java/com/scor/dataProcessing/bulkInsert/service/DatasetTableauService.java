package com.scor.dataProcessing.bulkInsert.service;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scor.dataProcessing.bulkInsert.inputdata.service.InjectionPolicyService;
import com.scor.dataProcessing.bulkInsert.inputdata.service.InjectionProductService;
import com.scor.dataProcessing.bulkInsert.repository.InputDataRepository;
import com.scor.persistance.entities.DataSetEntity;
import com.scor.persistance.entities.RefUserEntity;
import com.scor.persistance.entities.StudyEntity;
import com.scor.persistance.repositories.DatasetRepository;

@Service
public class DatasetTableauService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -902759529625949465L;
	
	@Autowired
	private InjectionPolicyService injectionPolicyService;
	
	@Autowired
	private InjectionProductService injectionProductService;
	
//	@Autowired
//	private DatasetTableauRepository repository;
	
	@Autowired
	private
	DatasetRepository datasetRepository ;
	
	@Autowired
	private InputDataRepository inputDataRepository;
	
	@Autowired
	private UserHabilitationService habilitationService;
	
	public void manageInputDataStorage(DataSetEntity datasetEntity) {
//		Dataset dataset = new Dataset(datasetEntity);
//		dataset = this.save(dataset);
		inputDataRepository.deletePolicy(datasetEntity.getDsId());
		inputDataRepository.deleteProduct(datasetEntity.getDsId());
		habilitationService.deleteByDatasetId(datasetEntity.getDsId());
		injectionPolicyService.inject(datasetEntity.getDsEventExposureFile().getEafLink(), datasetEntity.getDsId());
		injectionProductService.inject(datasetEntity.getDsProductFile().getEafLink(), datasetEntity.getDsId());
		habilitationService.manageDatasetHabilitations(datasetEntity.getDsId());
	}
	

	
//	private Dataset save(Dataset dataset) {
////		Dataset ds = repository.findOne(dataset.getId());
//		if(ds != null) {
//			inputDataRepository.deletePolicy(dataset.getId());
//			inputDataRepository.deleteProduct(dataset.getId());
//			habilitationService.deleteByDatasetId(dataset.getId());
//			//TODO update dataset
//			return dataset;
//		}
////		repository.save(dataset);
//		return dataset;
//	}
	
	public void manageInputDataDeletion(DataSetEntity datasetEntity) {
		inputDataRepository.deletePolicy(datasetEntity.getDsId());
		inputDataRepository.deleteProduct(datasetEntity.getDsId());
		habilitationService.deleteByDatasetId(datasetEntity.getDsId());
//		repository.delete(datasetEntity.getDsId());
	}
	
	public void recalculateHabilitation(int dsId) {
		DataSetEntity dataset = datasetRepository.findOne(dsId);
		if(dataset == null) {
			return;
		}
		habilitationService.deleteByDatasetId(dsId);
		habilitationService.manageDatasetHabilitations(dsId);
	}
	
//	public Dataset findById(Integer dsId) {
//		if(dsId == null) {
//			return null;
//		}
//		return repository.findOne(dsId);
//	}

	public void recalculateHabilitationForUser(StudyEntity study, RefUserEntity user, int dsId) {
		DataSetEntity dataset = datasetRepository.findOne(dsId);
		if(dataset == null) {
			return;
		}
		try {
			habilitationService.recalculateHabilitationForUserByDsId(study, user, dsId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
