package com.scor.persistance.services;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scor.dataProcessing.bulkInsert.inputdata.service.InjectionIbnrAllocationService;
import com.scor.dataProcessing.bulkInsert.inputdata.service.InjectionIbnrAmountService;
import com.scor.dataProcessing.bulkInsert.inputdata.service.InjectionIbnrUdfService;
import com.scor.dataProcessing.bulkInsert.repository.ResultRepository;
import com.scor.dataProcessing.bulkInsert.service.ResultRunService;
import com.scor.persistance.entities.ConfigurationEntity;
import com.scor.persistance.entities.DecrementExpectedTableEntity;
import com.scor.persistance.entities.DecrementParametersEntity;
import com.scor.persistance.entities.RefExpectedTableEntity;
import com.scor.persistance.entities.RunCalcEntity;
import com.scor.persistance.entities.RunEntity;
import com.scor.persistance.repositories.RunCalcRepository;
import com.scor.persistance.repositories.RunRepository;
import com.scor.sas.services.RunSasService;

import scala.tools.nsc.Global.Run;

@Service
public class RunService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9152859950275722865L;

	private static final Logger LOGGER = Logger.getLogger(RunService.class);
	
	@Autowired
	private RunRepository repository;

	@Autowired
	private RunCalcRepository rp;

	@Autowired
	private RunSasService rs;
	
	@Autowired
	private ExpectedTableService es;

	@Autowired
	private ResultRunService resultRunService;
	
	@Autowired
	private ConfigurationService configurationService;
	@Autowired 
	InjectionIbnrUdfService injectionIbnrUdfService;
	@Autowired 
	InjectionIbnrAllocationService injectionIbnrAllocationService;
	@Autowired 
	InjectionIbnrAmountService injectionIbnrAmountService; 
	@Autowired 
	ResultRepository resultRepository;

	public List<RunEntity> getAll() {
		List<RunEntity> list = new ArrayList<RunEntity>();

		for (RunEntity run : repository.findAll()) {
			list.add(run);
		}
		return list;
	}

	public RunEntity save(RunEntity run) {
		MutableBoolean injectIbnrUdf = new MutableBoolean(true);
		MutableBoolean injectIbnrAllocation =  new MutableBoolean(true);
		MutableBoolean injectIbnrAmount =  new MutableBoolean(true);
		if (run.getRunId() >= 0) {
			deleteRunCalcByRunId(run.getRunId());
			resultRunService.deleteRunAndResult(run.getRunId());

			deleteIbnrIfExists(run, injectIbnrUdf, injectIbnrAllocation, injectIbnrAmount);
		}
		updateDecrements(run);
		RunEntity runEntity = repository.save(run);
		injectIBNR(runEntity, injectIbnrUdf.booleanValue(), injectIbnrAllocation.booleanValue(), injectIbnrAmount.booleanValue());
		return runEntity;
	}
	
	private void deleteIbnrIfExists(RunEntity run, MutableBoolean injectIbnrUdf, MutableBoolean injectIbnrAllocation,
			MutableBoolean injectIbnrAmount) {
		RunEntity persistentRun = repository.findOne(run.getRunId());
		if (persistentRun != null){
			if (StringUtils.isNotBlank(run.getRunIbnrManuelUdf())
					&& run.getRunIbnrManuelUdf().equalsIgnoreCase(persistentRun.getRunIbnrManuelUdf())) {
				injectIbnrUdf.setValue(false);
			} else {
				if (StringUtils.isNotBlank(persistentRun.getRunIbnrManuelUdf()))
					resultRepository.deleteIbnrUdf(run.getRunId());
			}
			if (StringUtils.isNotBlank(run.getRunIbnrAllocation())
					&& run.getRunIbnrAllocation().equalsIgnoreCase(persistentRun.getRunIbnrAllocation())) {
				injectIbnrAllocation.setValue(false);
			} else {
				if (StringUtils.isNotBlank(persistentRun.getRunIbnrAllocation()))
					resultRepository.deleteIbnrAllocation(run.getRunId());
			}
			if (StringUtils.isNotBlank(run.getRunIbnrAmount())
					&& run.getRunIbnrAmount().equalsIgnoreCase(persistentRun.getRunIbnrAmount())) {
				injectIbnrAmount.setValue(false);
			} else {
				if (StringUtils.isNotBlank(persistentRun.getRunIbnrAmount()))
					resultRepository.deleteIbnrAmount(run.getRunId());
			}
		}


	}

	private void injectIBNR(RunEntity run, boolean injectIbnrUdf, boolean injectIbnrAllocation,
			boolean injectIbnrAmount) {
		if ("manual".equalsIgnoreCase(run.getRunIbnrMethod())) {
			if (injectIbnrUdf && StringUtils.isNotBlank(run.getRunIbnrManuelUdf()))
				injectionIbnrUdfService.inject(run.getRunIbnrManuelUdf(), run.getRunId());
		} else {
			if (injectIbnrAllocation && StringUtils.isNotBlank(run.getRunIbnrAllocation()))
				injectionIbnrAllocationService.inject(run.getRunIbnrAllocation(), run.getRunId());
			if (injectIbnrAmount && StringUtils.isNotBlank(run.getRunIbnrAllocation()))
				injectionIbnrAmountService.inject(run.getRunIbnrAmount(), run.getRunId());
		}
	}

	private void updateDecrements(RunEntity run) {
		Collection<DecrementParametersEntity> decrements = run.getDecrementParametersByRunId();
		if (decrements != null) {
			decrements.forEach(decrement -> {
				try {
					if (StringUtils.isNotBlank(decrement.getDpExpMethod())
							&& decrement.getDpExpMethod().toLowerCase().trim().equals("us_method")
							&& StringUtils.isNotBlank(decrement.getDbCalibrationUrl())) {
						String calibrationFilePath = decrement.getDbCalibrationUrl();
						Stream<String> lines = readFileContent(calibrationFilePath);
						List<String> headers = loadFileHeaderLowerCase(calibrationFilePath);
						AtomicInteger index = new AtomicInteger(1);
						lines.forEach(line -> {
							DecrementExpectedTableEntity exptable = new DecrementExpectedTableEntity();
							String[] array = line.split(";");
							if (headers.contains("policy_table_id")) {
								String policyTableId = array[headers.indexOf("policy_table_id")];
								RefExpectedTableEntity policyTable = es.getByCode(policyTableId);
								if (policyTable != null && "policy".equalsIgnoreCase(policyTable.getRetType())) {
									exptable.setRefExpectedTableByRetPolicy(policyTable);
									exptable.setRetPolicy(policyTable.getRetId());
								}
							}
							if (headers.contains("trend_table_id")) {
								String trendTableId = array[headers.indexOf("trend_table_id")];
								if (StringUtils.isNotBlank(trendTableId)) {
									RefExpectedTableEntity trendTable = es.getByCode(trendTableId);
									if (trendTable != null && "trend".equalsIgnoreCase(trendTable.getRetType())) {
										exptable.setRefExpectedTableByRetTrend(trendTable);
										exptable.setRetTrend(trendTable.getRetId());
									}
								}
							}
							exptable.setRetBasis(index.getAndIncrement());
							decrement.getDecrementExpectedTablesByDpId().add(exptable);
						});
					}
				} catch (IOException e) {

				}
			});
		}
	}

	public RunEntity saveExp(RunEntity run) {
//		if (run.getRunId() >= 0) {
//
//			deleteRunCalcByRunId(run);
//		}

		return repository.save(run);
	}

	private void deleteRunCalcByRunId(int runId) {
		rp.deleteAllByRclcRunId(runId);
	}
	
	public RunEntity saveParameter(RunEntity run) {
		RunEntity runToSave = repository.findByRunId(run.getRunId());
		runToSave.setRunDescription(run.getRunDescription());
		return repository.save(runToSave);
	}

	public RunCalcEntity getRunCalcById(Integer id) {
		List<RunCalcEntity> calc = rp.findByRclcRunIdOrderByRclcStartDateDesc(id);
		return (!calc.isEmpty()) ? calc.get(0) : null;
	}

	public RunCalcEntity createRunCalc(int id) {
		this.deleteRunCalcByRunId(id);
		ConfigurationEntity configurationEntity = configurationService.getVersion();
		RunCalcEntity rr = new RunCalcEntity();
		rr.setRclcStatus("PENDING");
		rr.setRclcStartDate(new Timestamp(System.currentTimeMillis()));
		rr.setRclcRunId(id);
		String rclcVersion = "v" + configurationEntity.getGlobalVersion() + "." + configurationEntity.getSasVersion() + "." + configurationEntity.getEaVersion();
		rr.setRclcVersion(rclcVersion);
		rp.save(rr);
		return rr;
	}

	public List<RunEntity> getByDatasetId(Integer id) {
		return repository.findByRunDsId(id);
	}

	public List<RunEntity> getByStudyId(Integer id) {
		List<RunEntity> runs = repository.findByRunStId(id);

		runs.forEach(runEntity -> runEntity.setRunCalcEntity(getRunCalcById(runEntity.getRunId())));

		return runs;
	}

	public RunEntity getByRunId(Integer id) {
		return repository.findByRunId(id);
	}

	public void delete(int id) {
		rs.deleteRunFolder(id);
		resultRunService.deleteRunAndResult(id);
		if (repository.exists(id)) {
			repository.delete(id);
		}
	}

	public RunCalcEntity updateRunStatus(int id, String val, String Description, String User, String EndDate,
			String step, String log, String statusEa) throws Exception {
		RunCalcEntity rr = rp.findByRclcId(id);
		if (rr == null) {
			LOGGER.error("Run Calc id doesn't exist");
			throw new Exception("Run Calc id doesn't exist");
		}
		if (val != null && !val.isEmpty())
			rr.setRclcStatus(val);
		if (Description != null && !Description.isEmpty())
			rr.setRclcDescription(Description);
		if (User != null && !User.isEmpty())
			rr.setRclcUserCreation(User);
		if (step != null && !step.isEmpty())
			rr.setRclcStep(step);
		if (EndDate != null && !EndDate.isEmpty() && EndDate.equalsIgnoreCase("fin")) {
			/*
			 * long ll = Long.parseLong(EndDate); ll = ll - 315619200;
			 */
			rr.setRclcEndDate(new Timestamp(System.currentTimeMillis()));
		}
		if (log != null && !log.isEmpty())
			rr.setRclcLogPath(log);
		if(StringUtils.isNotBlank(statusEa))
			rr.setRclcStatusEa(statusEa);
		rp.save(rr);
		return rr;
	}

	public List<RunEntity> manageStudyMasterRuns(int studyId, int masterRunQx, int masterRunIx, int masterRunWx, int masterRunIxQx,
			List<Integer> runsRetained) {
		List<RunEntity> listRun = getByStudyId(studyId);

		List<RunEntity> listRunToSave = new ArrayList<>();
		for (RunEntity run : listRun) {
			int id = run.getRunId();
			boolean oldMasterRunQx = run.getMasterRunQx() != null ? run.getMasterRunQx() : false;
			boolean oldMasterRunIx = run.getMasterRunIx() != null ? run.getMasterRunIx() : false;
			boolean oldMasterRunIxQx = run.getMasterRunIxQx() != null ? run.getMasterRunIxQx() : false;
			boolean oldMasterRunWx = run.getMasterRunWx() != null ? run.getMasterRunWx() : false;
			boolean oldRunRetained = run.getRunRetained() != null ? run.getRunRetained() : false;
			run.setMasterRunQx(id == masterRunQx);
			run.setMasterRunIx(id == masterRunIx);
			run.setMasterRunWx(id == masterRunWx);
			run.setMasterRunIxQx(id == masterRunIxQx);

//			if(runsRetained == null || !runsRetained.contains(id)) {
//				resultRunService.deleteNonMasterRunResult(id, id == masterRunQx, id == masterRunIx, id == masterRunWx, id == masterRunIxQx);
//			}
			run.setRunRetained(runsRetained != null && runsRetained.contains(id));
			if (run.getRunRetained().equals(oldRunRetained) && run.getMasterRunQx().equals(oldMasterRunQx)
					&& run.getMasterRunIx().equals(oldMasterRunIx) && run.getMasterRunIxQx().equals(oldMasterRunIxQx) && run.getMasterRunWx().equals(oldMasterRunWx)) {
				continue;
			}
			listRunToSave.add(run);
		}
		if (listRunToSave.isEmpty()) {
			return listRun;
		}
		repository.save(listRunToSave);
		new Thread(()->  {
			LOGGER.info("Delete non master Run Result for study = " + studyId + " BEGIN");
			for (RunEntity run : listRun) {
				int id = run.getRunId();
				if(runsRetained == null || !runsRetained.contains(id)) {
					resultRunService.deleteNonMasterRunResult(id, id == masterRunQx, id == masterRunIx, id == masterRunWx, id == masterRunIxQx);
				}
			}
			LOGGER.info("Delete non master Run Result for study = " + studyId + " END");
		});
		return listRun;
	}
	
	private Stream<String> readFileContent(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		Stream<String> lines = Files.lines(path, StandardCharsets.ISO_8859_1);
		return lines.skip(1);
	}
	
	private List<String> loadFileHeaderLowerCase(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		Stream<String> lines = Files.lines(path, StandardCharsets.ISO_8859_1);
		String[] lineArray = lines.findFirst().get().split(";");
		List<String> headers = new ArrayList<>();
		for (String line : lineArray) {
			headers.add(line.toLowerCase());
		}
		return headers;
	}

}
