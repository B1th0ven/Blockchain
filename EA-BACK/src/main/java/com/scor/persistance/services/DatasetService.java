package com.scor.persistance.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.scor.TableUpdateReport.UpdateService;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scor.dataProcessing.bulkInsert.service.DatasetTableauService;
import com.scor.persistance.entities.DataSetEntity;
import com.scor.persistance.entities.EaFilesEntity;
import com.scor.persistance.entities.RefUserEntity;
import com.scor.persistance.entities.RunEntity;
import com.scor.persistance.entities.StudyEntity;
import com.scor.persistance.repositories.DatasetRepository;
import com.scor.persistance.repositories.EaFilesRepository;

@Service
public class DatasetService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5760060573042568266L;

	@Autowired
	private DatasetRepository repository;
	
	@Autowired
	private EaFilesRepository filesRepository;

	@Autowired
	private RunService rs;

	@Autowired
	private UpdateService updateService;

	@Autowired
	private DatasetTableauService datasetTableauService;

	public DataSetEntity postDataset(DataSetEntity ds) {
		repository.save(ds);
		if( StringUtils.isNotBlank(ds.getDsTechReport()) || StringUtils.isNotBlank(ds.getDsFuncReport()) || StringUtils.isNotBlank(ds.getDsNotExecuted()) )  {
			updateService.updateControls(ds.getDsId());
		}
		return ds;
	}
	
	public DataSetEntity postDatasetReport(DataSetEntity ds) {
		DataSetEntity dataset = repository.save(ds);
		if (dataset != null && dataset.getDsDataAvailableTableau() != null && dataset.getDsDataAvailableTableau()) {
			new Thread(()-> {
				datasetTableauService.manageInputDataStorage(dataset);
			}).start();
		}
		return dataset;
	}

	public List<DataSetEntity> getAll() {
		List<DataSetEntity> list = new ArrayList<DataSetEntity>();

		for (DataSetEntity st : repository.findAll()) {
			list.add(st);
		}
		return list;
	}

	public boolean isStudyDatasetsValid(int id) {
		List<DataSetEntity> list = new ArrayList<DataSetEntity>();

		for (DataSetEntity st : repository.findByDsStId(id)) {
			if (st.getDsFuncReport() != null && st.getDsTechReport() != null) {
				return true;
			}
		}
		return false;
	}

	public int validStudyDatasetsCount(int id) {
		int count = 0;
		List<DataSetEntity> list = new ArrayList<DataSetEntity>();

		for (DataSetEntity st : repository.findByDsStId(id)) {
			// System.out.println(st.getDsName());
			if (st.getDsProductFile() != null && st.getDsEventExposureFile() != null && st.getDsFuncReport() != null
					&& st.getDsTechReport() != null)
				count++;

			// System.out.println(count);
		}

		return count;
	}

	public DataSetEntity getById(int id) {
		return this.repository.findOne(id);
	}

	public List<DataSetEntity> getDatasetsByStudyId(int id) {
		return repository.findByDsStId(id);
	}

	public void deleteDataset(int id) throws Exception {

		List<RunEntity> ll = rs.getByDatasetId(id);
		if (ll != null && ll.size() > 0)
			throw new Exception("Forbidden: Some runs use the dataset with id: " + id);
		DataSetEntity dataset = repository.findOne(id);
		if (dataset != null) {
			EaFilesEntity dsEventExposureFile = dataset.getDsEventExposureFile();
			if (dsEventExposureFile != null) {
				dsEventExposureFile.setEafSubmitter(null);
				dataset.setDsEventExposureFile(dsEventExposureFile);
			}
			EaFilesEntity dsProductFile = dataset.getDsProductFile();
			if (dsProductFile != null) {
				dsProductFile.setEafSubmitter(null);
				dataset.setDsProductFile(dsProductFile);
			}
			repository.save(dataset);
//			if (repository.exists(id))
			repository.delete(id);
			new Thread(()-> {
				datasetTableauService.manageInputDataDeletion(dataset);
			}).start();
		} else {
			throw new Exception("Unable to delete dataset with id: " + id);
		}
	}

	public List<DataSetEntity> isInputFilesUniqueForDataset(int id) {
		DataSetEntity ds = repository.findOne(id);
		List<DataSetEntity> res = repository.findByDsEventExposureFile(ds.getDsEventExposureFile());
		return res;
	}

	public List<DataSetEntity> findDatasetByFileId(String path) {
		List<EaFilesEntity> files = filesRepository.findByEafLink(path);

		for (EaFilesEntity eaFilesEntity : files) {
			List<DataSetEntity> datasets = repository.findByDsEventExposureFileOrDsProductFile(eaFilesEntity, eaFilesEntity);
			if(datasets == null || datasets.isEmpty()) {
				continue;
			}
			return datasets;
		}
		
		return new ArrayList<>();
	}
	
	public void recalculateHabilitation(int dsId) {
		datasetTableauService.recalculateHabilitation(dsId);
	}

	public void recalculateHabilitationForUser(StudyEntity study, RefUserEntity user, int dsId) {
		datasetTableauService.recalculateHabilitationForUser(study,user, dsId);
		
	}
	// valide pour le sprint 11 :
	public List<String> validateListDecrement(List<String> decrements, int datasetId) {
		try {
			DataSetEntity dataSet = repository.findOne(datasetId);
			if (dataSet == null || StringUtils.isBlank(dataSet.getDsFuncReport())) {
				return decrements;
			}
			String report = dataSet.getDsFuncReport();
			JSONObject jsonObj = new JSONObject(report);
			JSONObject fileValues = jsonObj.getJSONObject("fileValues");
			List<String> accelerationRiskType = new ArrayList<>();
			try {
				accelerationRiskType = jsonArrayToList(fileValues, "acceleration_risk_type");
			} catch (Exception e) {
			}
			List<String> mainRiskType = new ArrayList<>();
			try {
				mainRiskType = jsonArrayToList(fileValues, "main_risk_type");
			} catch (Exception e) {
			}
			if(mainRiskType != null && !mainRiskType.contains("life")) {
				decrements.remove("qx");
			}
			for (String value : accelerationRiskType) {
				if(StringUtils.isNotBlank(value)) {
					return decrements;
				}
			}
			for (String value : mainRiskType) {
				if(value.equals("ci") || value.equals("di") || value.equals("ltc") || value.equals("tpd")) {
					return decrements;
				}
			}
			decrements.remove("ix");
			return decrements;
		} catch (JSONException e) {
			return decrements;
		}
	}

	private List<String> jsonArrayToList(JSONObject fileValues, String col) throws JSONException {
		JSONArray arr = fileValues.getJSONArray(col);
		ArrayList<String> list = new ArrayList<String>();
		if (arr != null) {
			int len = arr.length();
			for (int i = 0; i < len; i++) {
				list.add(arr.get(i).toString());
			}
		}
		return list;
	}

}
