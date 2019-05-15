package com.scor.sas.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sas.iom.SAS.ILanguageService;
import com.sas.iom.SAS.IWorkspace;
import com.sas.iom.SASIOMDefs.GenericError;
import com.scor.dataProcessing.bulkInsert.resultdata.service.ResultService;
import com.scor.dataProcessing.bulkInsert.service.ResultRunService;
import com.scor.dataProcessing.bulkInsert.service.UserHabilitationService;
import com.scor.dataProcessing.sparkConnection.Connection;
import com.scor.persistance.entities.DataSetEntity;
import com.scor.persistance.entities.DecrementExpectedTableEntity;
import com.scor.persistance.entities.DecrementParametersEntity;
import com.scor.persistance.entities.EaFilesEntity;
import com.scor.persistance.entities.RefExpectedTableEntity;
import com.scor.persistance.entities.RunCalcEntity;
import com.scor.persistance.entities.RunEntity;
import com.scor.persistance.repositories.RunDecrementParametersRepository;
import com.scor.persistance.repositories.RunRepository;
import com.scor.persistance.services.DatasetService;
import com.scor.persistance.services.ExpectedTableService;
import com.scor.persistance.services.RunService;
import com.scor.sas.entities.SasConnectionWrapper;
import com.scor.sas.exception.SasException;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

@Service
@Transactional
public class RunSasService implements Serializable {

    // private static ReentrantLock lock = new ReentrantLock();

    /**
     *
     */
    private static final long serialVersionUID = 2512770272690357458L;

    private static final Logger LOGGER = Logger.getLogger(RunSasService.class);

    @Autowired
    SasConnection CCC;

    @Autowired
    RunService rs;

    @Autowired
    DatasetService ds;

    @Autowired
    CodeBuilder cb;

    @Autowired
    ExpectedTableService expService;

    @Autowired
    private RunDecrementParametersRepository decrementParametreRepository;
    @Autowired
    private RunRepository runRepository ;
    @Autowired
    private ResultService resultService;

    @Autowired
    private ResultRunService resultRunService;

    @Autowired
    private UserHabilitationService userHabilitationService;

    @Value("${sas-run-repo}")
    private String run_repo;

    @Value("${sas-user}")
    private String sas_username;

    @Value("${sas-pass}")
    private String sas_password;

    @Value("${sas-domain}")
    private String sas_domain;
    
    @Value("${sas-shared-folder}")
    private String sas_shared_folder;

    public RunSasService() {
    }

    public RunCalcEntity run(String id, String Alldim, String dimensions) throws Exception {
        Long startRun = new Date().getTime();
        LOGGER.info("Start Lanching Run " + id);
        if (id == null || !id.matches("^\\d+$"))
            throw new Exception("Failed");
        String dropDim = defineDropDimensions(Alldim,dimensions);
        RunEntity run = rs.getByRunId(Integer.parseInt(id));
        DataSetEntity dataset = ds.getById(run.getRunDsId());
        EaFilesEntity dsEventExposureFile = dataset.getDsEventExposureFile();
        String path_policy = dsEventExposureFile.getEafLink();
        EaFilesEntity dsProductFile = dataset.getDsProductFile();
        String path_product = dsProductFile.getEafLink();
        String type = dataset.getDsDataStructureType().trim().toUpperCase();
        String path_udf = run.getRunIbnrManuelUdf();
        String path_amount = run.getRunIbnrAmount();
        String path_allocation = run.getRunIbnrAllocation();
        List<Collection<DecrementExpectedTableEntity>> decs = new ArrayList<>();

        Collection<DecrementParametersEntity> decrementParameters = run.getDecrementParametersByRunId();
//		Map<Integer , RefExpectedTableEntity> calibrationList = new HashMap<>();
        Map<Integer, String> calibrationUrlList = new HashMap<>();
        decrementParameters.stream().forEach(s -> {
            if (s.getDpExpMethod() != null && s.getDpExpMethod().equalsIgnoreCase("default_method")) {
                decs.add(s.getDecrementExpectedTablesByDpId());
            } else if (s.getDpExpMethod() != null && s.getDpExpMethod().equalsIgnoreCase("us_method")) {
                //	RefExpectedTableEntity calibration = expService.getOne(s.getDbExpCalibration());
                calibrationUrlList.put(s.getDpId(), s.getDbCalibrationUrl());
            }
        });

//        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(sas_domain, sas_username, sas_password);
        RunCalcEntity rr = rs.createRunCalc(Integer.parseInt(id));

        rs.updateRunStatus(rr.getRclcId(), "PENDING", null, null, null, null, run_repo + id + "\\\\output", "PENDING");

        new Thread(() -> {

            // Generate the sas cmd from data/run.sas to launch the SAS program
            String res;
            if (dropDim != null)
                res = cb.getCode(id, dropDim, type, rr.getRclcId());
            else
                res = cb.getCode(id, "", type, rr.getRclcId());

            try {
                rs.updateRunStatus(rr.getRclcId(), "PENDING", "CREATING RUN FOLDER", null, null, "CREATING RUN FOLDER",
                        null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // create the run folder on the SAS server
            String path_run = sas_shared_folder + id + "/";
            String path_input = sas_shared_folder + id + "/input/";
            String path_output = sas_shared_folder + id + "/output/";
            File sFile = null;
            try {
                sFile = new File(path_run);
                if (sFile.exists())
                    sFile.delete();
                sFile.mkdirs();
                sFile = new File(path_input);
                if (sFile.exists())
                    sFile.delete();
                sFile.mkdirs();
                sFile = new File(path_output);
                if (sFile.exists())
                    sFile.delete();
                sFile.mkdirs();

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Copy the input files to the run folder
            try {
                rs.updateRunStatus(rr.getRclcId(), "PENDING", "COPYING INPUT FILES", null, null, "COPYING INPUT FILES",
                        null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                copy(id, path_policy, "policy.csv");
            } catch (Exception e) {
                throwCopyError(rr.getRclcId(), "POLICY");
                return;
            }

            try {
                copy(id, path_product, "product.csv");
            } catch (Exception e) {
                throwCopyError(rr.getRclcId(), "PRODUCT");
                return;
            }

            if (path_udf != null && !path_udf.isEmpty()) {
                try {
                    copy(id, path_udf, "ibnr_udf.csv");
                } catch (Exception e) {
                    throwCopyError(rr.getRclcId(), "IBNR UDF");
                    return;
                }
            }
            if (path_allocation != null && !path_allocation.isEmpty()) {
                try {
                    copy(id, path_allocation, "ibnr_allocation.csv");
                } catch (Exception e) {
                    throwCopyError(rr.getRclcId(), "IBNR ALLOCATION");
                    return;
                }
            }
            if (path_amount != null && !path_amount.isEmpty()) {
                try {
                    copy(id, path_amount, "ibnr_amount.csv");
                } catch (Exception e) {
                    throwCopyError(rr.getRclcId(), "IBNR AMOUNT");
                    return;
                }
            }

            // copying expected if exists
            decs.stream().forEach(dec -> {
                dec.stream().forEach(exp -> {

                    if (exp.getRetBase() != null) {
                        RefExpectedTableEntity baseTable = expService.getOne(exp.getRetBase());
                        if (baseTable.getRetUrl() != null && !baseTable.getRetUrl().isEmpty()) {
                            try {
                                copy(id, baseTable.getRetUrl(), "base_" + exp.getDecetId() + ".csv");
                            } catch (Exception e) {
                                throwCopyError(rr.getRclcId(), "EXPECTED BASE");
                                return;
                            }
                        }
                    }
                    if (exp.getRetTrend() != null) {
                        RefExpectedTableEntity trendTable = expService.getOne(exp.getRetTrend());
                        if (trendTable.getRetUrl() != null && !trendTable.getRetUrl().isEmpty()) {
                            try {
                                copy(id, trendTable.getRetUrl(), "trend_" + exp.getDecetId() + ".csv");
                            } catch (Exception e) {
                                throwCopyError(rr.getRclcId(), "EXPECTED TREND");
                                return;
                            }
                        }
                    }
                    if (exp.getRetAdjustment() != null) {
                        RefExpectedTableEntity adjustmentTable = expService.getOne(exp.getRetAdjustment());
                        if (adjustmentTable.getRetUrl() != null && !adjustmentTable.getRetUrl().isEmpty()) {
                            try {
                                copy(id, adjustmentTable.getRetUrl(), "adjustment_" + exp.getDecetId() + ".csv");
                            } catch (Exception e) {
                                throwCopyError(rr.getRclcId(), "EXPECTED ADJUSTMENT");
                                return;
                            }
                        }
                    }

                });
            });
            calibrationUrlList.keySet().forEach(decrementId -> {
                String calibrationTableUrl = calibrationUrlList.get(decrementId);
                if (calibrationTableUrl != null && !calibrationTableUrl.isEmpty()) {
                    String path_input_us = sas_shared_folder + id + "/input/" + decrementId + "/";
                    try {
                        File cFile = new File(path_input_us);
                        if (cFile.exists())
                            cFile.delete();
                        cFile.mkdirs();
                        copy(id, calibrationTableUrl, decrementId + "/expected_basis_calibration.csv");
                        copyingPolicyandTrend(id, calibrationTableUrl, decrementId, rr.getRclcId());
                    } catch (Exception e) {
                        throwCopyError(rr.getRclcId(), "EXPECTED CALIBRATION");
                        return;
                    }
                }
            });

            try {
                rs.updateRunStatus(rr.getRclcId(), "LAUNCHING", "LAUNCHING THE RUN ON THE SAS SERVER", null, null,
                        "LAUNCHING THE RUN ON THE SAS SERVER", null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Submit the cmd to the SAS server
            SasConnectionWrapper con = null;
            try {

                con = CCC.getConnectionWrapperByWorkspaceFactory();
                IWorkspace iw = con.getIWorkspace();
                ILanguageService il = iw.LanguageService();
                il.Submit(res);
                /*
                 * CarriageControlSeqHolder logCCHldr = new CarriageControlSeqHolder();
                 * LineTypeSeqHolder logLTHldr = new LineTypeSeqHolder(); StringSeqHolder
                 * logHldr = new StringSeqHolder(); il.FlushLogLines(Integer.MAX_VALUE,
                 * logCCHldr, logLTHldr, logHldr); String[] logLines = logHldr.value;
                 */
                // System.out.println(Arrays.toString(logLines));
                LOGGER.info("run finish copying " + id);

            } catch (SasException e) {
                LOGGER.error("run failed " + id);

                e.printStackTrace();
            } catch (GenericError genericError) {
                LOGGER.error("run failed " + id);

                genericError.printStackTrace();
            } finally {
                Long endRun = new Date().getTime();
                LOGGER.info("run launched on sas server" + id + " in : " + (endRun - startRun) + " ms");
            }
        }).start();

        return rr;

    }

    private String defineDropDimensions(String Alldimensions, String dimensions) {
    	if(StringUtils.isBlank(Alldimensions)) {
    		return "";
    	}
    	String[] allDim = Alldimensions.split(" ",-1);
    	String[] dim = dimensions.split(" ",-1);
    	List<String> dimensionsList = Arrays.asList(dim);
    	String dropedDim = "";
    	for (String d : allDim) {
			if(dimensionsList.contains(d)) {
				continue;
			}
			dropedDim += d + " ";
		}
		return dropedDim;
	}

	private void copyingPolicyandTrend(String id, String retUrl, Integer decrementId, int runCalcId) {
        JavaSparkContext sc = Connection.getContext();

        JavaRDD<String> data = sc.textFile(retUrl);

        String header = data.first();
        List<String> names = Arrays.asList(header.toLowerCase().split(";", -1));

        List<String> policyIds = data.filter(line -> (!line.equalsIgnoreCase(header) && StringUtils.isNotBlank(line)))
                .map(line -> line.split(";")[names.indexOf("policy_table_id")]).distinct().collect();
        List<String> trendIds = data.filter(line -> (!line.equalsIgnoreCase(header) && StringUtils.isNotBlank(line)))
                .map(line -> line.split(";")[names.indexOf("trend_table_id")]).distinct().collect();
        for (String policy : policyIds) {
            RefExpectedTableEntity policyTable = expService.getByCode(policy);
            try {
                copy(id, policyTable.getRetUrl(), decrementId + "/policy_" + policy + ".csv");
            } catch (Exception e) {
                throwCopyError(runCalcId, "EXPECTED POLICY" + decrementId + "/policy_" + policy + ".csv");
                return;
            }
        }
        for (String trend : trendIds) {
			if (StringUtils.isBlank(trend)) {
				continue;
			}
            RefExpectedTableEntity trendTable = expService.getByCode(trend);
            try {
                copy(id, trendTable.getRetUrl(), decrementId + "/trend_" + trend + ".csv");
            } catch (Exception e) {
                throwCopyError(runCalcId, "EXPECTED TREND" + decrementId + "/trend_" + trend + ".csv");
                return;
            }
        }

    }

    public RunCalcEntity getRunById(int id) {
        return rs.getRunCalcById(id);
    }

    private void copy(String id, String path, String filename) throws Exception {
        Date dateBegin = new Date();
        Long timeBegin = dateBegin.getTime();
        LOGGER.info("Run " + id + " : Begin copying file " + filename);
        Path source = Paths.get(path);
        File newFile = null;
        
        newFile = new File(sas_shared_folder + id + "/input/" + filename);
        
        try (OutputStream out = new FileOutputStream(newFile)){
            Files.copy(source, out);
            Date dateEnd = new Date();
            Long timeEnd = dateEnd.getTime();
            LOGGER.info("Run " + id + " : End copying file " + filename + " in : " + (timeEnd - timeBegin) + " ms");
        } catch (IOException e) {
            LOGGER.error("Run " + id + " : End copying file " + filename + " with error on IOException");
            // e.printStackTrace();
            throw e;
        }

    }

    public File download(int runid) throws Exception {
        String path_output = sas_shared_folder + runid + "/output/EA_result_" + runid
                + ".csv";
        File sFile = new File(path_output);
        return sFile;
    }

    public File downloadXlsx(int runid) throws Exception {
        String path_output = sas_shared_folder + runid + "/output/EA_result_" + runid
                + ".xlsx";
        File sFile = new File(path_output);
        return sFile;
    }

    public File downloadError(int runid) throws Exception {
        String path_output = sas_shared_folder + runid + "/output/Failed_"+runid+".xlsx";
        File sFile = new File(path_output);
        if (!sFile.exists()) {
            path_output = sas_shared_folder + runid + "/output/Failed_"+runid+".csv";
            sFile = new File(path_output);
            if (!sFile.exists()) throw new Exception("File does not exist");
        }
        return sFile;
    }

    public boolean isFileErrorExist(int runid){
        String path_output = sas_shared_folder + runid + "/output/Failed_"+runid+".xlsx";
        File sFile = new File(path_output);
        if (!sFile.exists()) {
            path_output = sas_shared_folder + runid + "/output/Failed_"+runid+".csv";
            sFile = new File(path_output);
            if (!sFile.exists()) return false;
        }
        return true;
    }


    public RunCalcEntity update(int id, String status, String Description, String User, String EndDate, String step) throws Exception {
        RunCalcEntity runCalc = new RunCalcEntity();

        if (status.trim().equalsIgnoreCase("done")) {
            try {
                String statusEa = "Synchronizing Result";
                runCalc = rs.updateRunStatus(id, status, "Injecting Run Results From SAS", User, null, step, null, statusEa);
                Date dateBegin = new Date();
                LOGGER.info("Run " + runCalc.getRclcRunId() + " uppdate status");
                RunEntity runEntity= runRepository.findOne(runCalc.getRclcRunId()) ; 
                runEntity.setRunStatus("done");
                runRepository.save(runEntity) ;
      //         Run run = resultRunService.findOrSaveRun(runCalc.getRclcRunId());
                resultService.onStatusDone(runCalc.getRclcRunId(), 0);
                statusEa = "DONE";
                runCalc = rs.updateRunStatus(id, status, Description, User, EndDate, step, null, statusEa);
                Date dateEnd = new Date();
                LOGGER.info("Run " + runCalc.getRclcRunId() + " is saved in db in "
                        + (dateEnd.getTime() - dateBegin.getTime()) + " ms");
                int rclcRunId = runCalc.getRclcRunId();
                new Thread(() -> {
					userHabilitationService.run(rclcRunId);
                }).start();
            } catch (Exception e) {
                String statusEa = "Error While Synchronizing Result";
                runCalc = rs.updateRunStatus(id, status, e.getMessage(), User, EndDate, step, null, statusEa);
            }
        } else {
            String statusEa = "";
            runCalc = rs.updateRunStatus(id, status, Description, User, EndDate, step, null, statusEa);
        }
        return runCalc;
    }
    
	public void calculateHabilitations() {
		List<RunEntity> runs = runRepository.findAllByRunStatus("done");
		runs.stream().map(RunEntity::getRunId).forEach(id -> {
			userHabilitationService.deleteByRunId(id);
			userHabilitationService.run(id);
		});

	}

    public void deleteRunFolder(int id) {
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(sas_domain, sas_username, sas_password);
        String path = "smb:" + run_repo.replaceAll("\\\\\\\\", "/") + id + "/";
        try {
            SmbFile sFile = new SmbFile(path, auth);

            if (sFile.exists())
                sFile.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void throwCopyError(int runCalcId, String filename) {
        try {
            rs.updateRunStatus(runCalcId, "ERROR", "COULD NOT FIND THE " + filename + " FILE", null, "fin",
                    "COPYING INPUT FILES", null, "PENDING");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void testInsert(String path) {
        resultService.insertResult(path);
    }

}
