package com.scor.dataProcessing.bulkInsert.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scor.dataProcessing.bulkInsert.repository.ResultRepository;
import com.scor.dataProcessing.bulkInsert.repository.UserRunAccessRepository;
import com.scor.dataProcessing.common.StudyRoles;
import com.scor.dataProcessing.common.StudyRoles.SecondaryRole;
import com.scor.persistance.entities.DataSetEntity;
import com.scor.persistance.entities.RefUserEntity;
import com.scor.persistance.entities.RunEntity;
import com.scor.persistance.entities.StudyEntity;
import com.scor.persistance.repositories.DatasetRepository;
import com.scor.persistance.repositories.StudyRepository;
import com.scor.persistance.services.RoleManagerService;
import com.scor.persistance.services.RunService;
import com.scor.persistance.services.UserService;

@Service
public class UserHabilitationService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5923279498992260230L;
//	public boolean locked = false;
	@Autowired
	private UserService userService;
	@Autowired
	private RunService runService;
	@Autowired
	private RoleManagerService roleManagerService;
	@Autowired
	private ResultRepository resultRepository;
	@Autowired
	private StudyRepository studyRepository;
	@Autowired
	private UserRunAccessRepository repository;
	@Autowired
	private DatasetRepository datasetRepository;
	
	private final static Logger LOGGER = Logger.getLogger(UserHabilitationService.class);

	public synchronized void run(int runId) {
		LOGGER.info("Begin Calculating User Result habilation for run : " + runId);
		Date dateBegin = new Date();
		RunEntity run = runService.getByRunId(runId);
		if(run == null) {
			return;
		}
		List<RefUserEntity> users = userService.getAll();
		StudyEntity study = studyRepository.findOne(run.getRunStId());
		if(study == null) {
			return;
		}
		users.forEach(user -> {
			try {
//				Integer id = resultRepository.getUserAccess(user.getRuId(), runId);
//				if (id == null || id == 0) {
					StudyRoles roles = roleManagerService.StudyPrevligesCalCulator(user, study);
					if (roles != null && roles.getSecondaryRoles() != null && !roles.getSecondaryRoles().isEmpty()) {
						if (roles.getSecondaryRoles().contains(SecondaryRole.TABLE_RESULT_READER)) {
							resultRepository.insertUserAccess(user.getRuId(), runId);
						}
					}
//				} else {
//					LOGGER.info("Access Right to Result already calculated for user : " + user.getRuId() + " for run : "
//							+ runId);
//				}
			} catch (Exception e) {
				LOGGER.error("Error encoutred while calculating access rights for user : " + user.getRuLogin() + " for run : " + runId);
			}
		});
		Date dateEnd = new Date();
		LOGGER.info("End Calculating User Result habilation for run : " + runId + " in : "
				+ (dateEnd.getTime() - dateBegin.getTime()) + " ms");
//		locked = false;
	}
	
	public void manageDatasetHabilitations(int dsId) {
		DataSetEntity dataset = datasetRepository.findOne(dsId);
		if(dataset == null) {
			return;
		}
		StudyEntity study = studyRepository.findOne(dataset.getDsStId());
		if(study == null) {
			return;
		}
		repository.deleteByDatasetId(dsId);
		List<RefUserEntity> users = userService.getAll();
		users.forEach(user -> {
			try {
				StudyRoles roles = roleManagerService.StudyPrevligesCalCulator(user, study);
				if (roles != null && roles.getSecondaryRoles() != null && !roles.getSecondaryRoles().isEmpty()) {
					if (roles.getSecondaryRoles().contains(SecondaryRole.TABLE_INPUT_READER)) {
						repository.saveDataset(dsId, user.getRuId());
					}
				}
			} catch (Exception e) {
				LOGGER.error("Error encoutred while calculating access rights for user : " + user.getRuLogin() + " for dataset : " + dsId);
			}
		});
	}
	
	public void deleteByDatasetId(int dsId) {
		repository.deleteByDatasetId(dsId);
	}
	
	
	public void deleteByRunId(int runId) {
		repository.deleteByRunId(runId);
	}

	public void recalculateHabilitationForUserByDsId(StudyEntity study, RefUserEntity user, int dsId) throws Exception {
		StudyRoles roles = roleManagerService.StudyPrevligesCalCulator(user, study);
		if (roles != null && roles.getSecondaryRoles() != null && !roles.getSecondaryRoles().isEmpty()
				&& roles.getSecondaryRoles().contains(SecondaryRole.TABLE_INPUT_READER)) {
			Integer id = repository.findByDsIdAndUserId(dsId, user.getRuId());
			if (id == null || id == 0) {
				repository.saveDataset(dsId, user.getRuId());
			}
		} else {
			Integer id = repository.findByDsIdAndUserId(dsId, user.getRuId());
			if (id != null && id != 0) {
				repository.deleteByDatasetIdAndUserId(dsId, user.getRuId());
			}
		}
	}
	
	public void recalculateHabilitationForUserByRunId(StudyEntity study, RefUserEntity user, int runId)
			throws Exception {
		StudyRoles roles = roleManagerService.StudyPrevligesCalCulator(user, study);
		if (roles != null && roles.getSecondaryRoles() != null && !roles.getSecondaryRoles().isEmpty()
				&& roles.getSecondaryRoles().contains(SecondaryRole.TABLE_RESULT_READER)) {
			Integer id = repository.findByRunIdAndUserId(runId, user.getRuId());
			if (id == null || id == 0) {
				repository.saveRun(runId, user.getRuId());
			}
		} else {
			Integer id = repository.findByRunIdAndUserId(runId, user.getRuId());
			if (id != null && id != 0) {
				repository.deleteByRunIdAndUserId(runId, user.getRuId());
			}
		}
	}

	public void deleteByUserId(int id) {
		repository.deleteByUserId(id);
	}

}
