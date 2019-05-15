package com.scor.dataProcessing.bulkInsert.service;

import java.io.Serializable;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.scor.dataProcessing.bulkInsert.repository.ResultRepository;

import com.scor.persistance.entities.RefUserEntity;
import com.scor.persistance.entities.RunEntity;
import com.scor.persistance.entities.StudyEntity;

import com.scor.persistance.repositories.RunRepository;
import com.scor.persistance.services.RunService;


@Service
public class ResultRunService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5389168311179895823L;
	@Autowired
	private ResultRepository resultRepository;
	@Autowired
	private RunService runService;
	@Autowired 
	private RunRepository runRepository ;
	
	@Autowired
	private UserHabilitationService habilitationService;


	private final static Logger LOGGER = Logger.getLogger(ResultRunService.class);

//	public Run findOrSaveRun(int runId) throws RuntimeException {
//		try {
//			connection.getConnection().setAutoCommit(false);
//			Run run = resultRepository.getRun(runId);
//			if (run != null) {
//				resultRepository.deleteResultByRunIdAndDecrement(runId, null);
//				habilitationService.deleteByRunId(runId);
//				return run;
//			}
//			RunEntity runEntity = runService.getByRunId(runId);
//			Study study = resultRepository.getStudy(runEntity.getRunStId());
//			if (study == null) {
//				StudyEntity studyEntity = studyService.getStudy(runEntity.getRunStId());
//				study = new Study(studyEntity);
//				resultRepository.insertStudy(study);
//
//			}
//			run = new Run(runEntity);
//			Dataset dataset = datasetTableauService.findById(runEntity.getRunDsId());
//			if (dataset == null) {
//				run.setDatasetId(null);
//			}
//			resultRepository.insertRun(run);
//			List<DecrementParametersEntity> decrementsEntity = runDecrementParametersRepository
//					.findByRunByDpRunId(runEntity);
//			decrementsEntity.stream().forEach(decrementEntity -> {
//				Decrement decrement = resultRepository.getDecrement(decrementEntity.getDpId());
//				if (decrement == null) {
//					decrement = new Decrement(decrementEntity);
//					try {
//						resultRepository.InsertDecrement(decrement);
//					} catch (Exception e) {
//						throw new RuntimeException(e) ; 
//					}
//				}
//			});
//
//			connection.getConnection().commit();
//			return run;
//		} catch (Exception e) {
//			LOGGER.error(e);
//		} finally {
//			try {
//				connection.getConnection().close();
//			} catch (SQLException e) {
//				LOGGER.error(e);
//			}
//		}
//		return null;
//	}

	public void deleteNonMasterRunResult(int runId, boolean isQxMaster, boolean isIxMaster, boolean isWxMaster, boolean isIxQxMaster) {
		if (!isQxMaster) {
			resultRepository.deleteResultByRunIdAndDecrement(runId, "qx");
		}
		if (!isWxMaster) {
			resultRepository.deleteResultByRunIdAndDecrement(runId, "wx");
		}
		if (!isIxMaster) {
			resultRepository.deleteResultByRunIdAndDecrement(runId, "ix");
		}
		if (!isIxQxMaster) {
			resultRepository.deleteResultByRunIdAndDecrement(runId, "ix+qx");
		}
	}
	
	public void deleteRunAndResult(int id) {
		resultRepository.deleteResultByRunIdAndDecrement(id, null);
		habilitationService.deleteByRunId(id);
//		resultRepository.deleteRun(id);
	}

	public void manageResultAfterStudyDeletion(int stId) {
		List<RunEntity> runs = runService.getByStudyId(stId);
		runs.forEach(run-> {
//			this.deleteRunAndResult(run.getRunId());
			runService.delete(run.getRunId());
		});
//		resultRepository.changeStudyStatus(stId,"deleted");
		
	}
	
//	public void validateStudy(int studyId) {
//		resultRepository.changeStudyStatus(studyId,"validated");
//	}
	
//	public List<Integer> getRunIds() {
//		return resultRepository.getRunsIds();
//	}
	
	public void recalculateHabilitation(int runId) {
		RunEntity run = runRepository.findOne(runId);
		if(run == null) {
			return;
		}
		habilitationService.deleteByRunId(runId);
		habilitationService.run(runId);
	}

	public void recalculateHabilitationForUser(StudyEntity study, RefUserEntity user, int runId) {
		RunEntity run = runRepository.findOne(runId);
		if(run == null) {
			return;
		}
		try {
			habilitationService.recalculateHabilitationForUserByRunId(study,user,runId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
