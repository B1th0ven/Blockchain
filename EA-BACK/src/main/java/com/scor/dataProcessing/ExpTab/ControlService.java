package com.scor.dataProcessing.ExpTab;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scor.dataProcessing.dataChecker.DCFactory;
import com.scor.dataProcessing.dataChecker.integrityChecker.IntegrityCheckerRegister;
import com.scor.dataProcessing.dataChecker.schemaChecker.SchemaCheckerRegister;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResults;
import com.scor.persistance.entities.RefExpectedTableEntity;
import com.scor.persistance.entities.StudyEntity;
import com.scor.persistance.services.ExpectedTableService;
import com.scor.persistance.services.RunService;
import com.scor.persistance.services.StudyService;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ControlService implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6212656251462326L;

	@Autowired
    ExpectedTableService es;

    @Autowired
    RunService rs;

    @Autowired
    StudyService ss;

    public List<String> checkBase(int base_id,int run_id,int study_id,String dimensions, boolean isDateOfCommencement, boolean isDateOfBirth, String exposureMetric, List<Integer> otherTables,boolean isAgeAtcom){


        List<String> errorReport = new ArrayList<String>();
        RefExpectedTableEntity base_entity = es.getOne(base_id);
//        RunEntity run_entity = rs.getByRunId(run_id);
        StudyEntity study_entity = ss.getStudy(study_id);



        List<String> base_dimensions = new ArrayList<String>();

        if(base_entity.getRetDimensions() != null && !base_entity.getRetDimensions().isEmpty())
         base_dimensions = Arrays.asList(base_entity.getRetDimensions().toLowerCase().split(";", -1));

        String missing_dims = "";
		for (String dim : base_dimensions) {
			if (!Arrays.asList(dimensions.toLowerCase().trim().split(";", -1)).contains(dim.toLowerCase().trim())) {
				missing_dims += dim + " ,";

			}
		}

        if(StringUtils.isNotBlank(missing_dims))
            errorReport.add("All dimensions must be present in Input Dataset and not be de-selected. Missing dimensions : "+StringUtils.substring(missing_dims, 0, missing_dims.length() - 1)+"/Blocking");

        if(!base_entity.getRetExposureMethod().equalsIgnoreCase(exposureMetric))
            errorReport.add("Exposure metric of run should match the exposure metric of the table. Exposure metric base table : "+base_entity.getRetExposureMethod()+"/Warning");


        if(!study_entity.getRefCountryById().getRcName().equalsIgnoreCase(base_entity.getCountry().getRcName()))
            errorReport.add("Country of study should match the country of the table. Country table : "+base_entity.getCountry().getRcName()+"/Warning");
        if(!isDateOfCommencement && es.checkColumnExistence(base_entity, "duration_year")) {
            errorReport.add("Table contains duration_year and requires date_of_commencement/Blocking");
        }
        if(!isDateOfBirth && es.checkColumnExistence(base_entity, "age_attained")) {
            errorReport.add("Table contains age_attained and requires date_of_birth/Blocking");
        }
        String mixAgeControls = mixAgeControls(base_entity, otherTables);
        if(StringUtils.isNotBlank(mixAgeControls)) {
        	errorReport.add(mixAgeControls);
        }
        if(!isAgeAtcom && es.checkColumnExistence(base_entity,"insurance_age_attained")) errorReport.add("Table contains insurance_age_attained and requires age_at_commencement, age_at_commencement_definition and date_of_commencement/Blocking");
        return errorReport;

    }

    private String mixAgeControls(RefExpectedTableEntity tableEntity, List<Integer> otherTables) {
    	String ageVariable = "";
    	if(es.checkColumnExistence(tableEntity, "age_attained")) {
    		ageVariable = "age_attained";
    	} else if(es.checkColumnExistence(tableEntity, "age_at_commencement")) {
    		ageVariable = "age_at_commencement";
    	} else if(es.checkColumnExistence(tableEntity, "insurance_age_attained")) {
    		ageVariable = "insurance_age_attained";
    	} else {
    		return null;
    	}
    	String errorMessage = "Tables contain incompatible age dimensions (age_attained & age_at_commencement or insurance_age_attained)/Blocking";
    	for (Integer tableId : otherTables) {
    		RefExpectedTableEntity table = es.getOne(tableId);
    		switch (ageVariable) {
			case "age_attained":
				if(es.checkColumnExistence(table, "age_at_commencement")) {
					return errorMessage;
				};
				if(es.checkColumnExistence(table, "insurance_age_attained")) {
					return errorMessage;
				};
				break;
			case "age_at_commencement":
				if(es.checkColumnExistence(table, "age_attained")) {
					return errorMessage;
				};
				break;
			case "insurance_age_attained":
				if(es.checkColumnExistence(table, "age_attained")) {
					return errorMessage;
				};
				break;

			default:
				break;
			}
		}
		return null;
	}

	public List<String> checkAdjTrend (int id,int base_id,String type,String dimensions, boolean isDateOfCommencement, boolean isDateOfBirth, List<Integer> otherTables,boolean isAgeAtcom ){

        List<String> errorReport = new ArrayList<String>();
        RefExpectedTableEntity base_entity = es.getOne(base_id);
        RefExpectedTableEntity TrendOrAdj_entity = es.getOne(id);

        if(base_entity != null && TrendOrAdj_entity != null && type.trim().equalsIgnoreCase("trend") && base_entity.getRetApplicationYear() != TrendOrAdj_entity.getRetApplicationYear())
            errorReport.add("Application year of table should be consistent with application year of base table. Application year table : "+TrendOrAdj_entity.getRetApplicationYear()+
                    ". Application year base table : "+base_entity.getRetApplicationYear()+"/Warning");

        List<String> trendOrAdj_dimensions = null;
        if(base_entity != null && TrendOrAdj_entity != null && TrendOrAdj_entity.getRetDimensions() != null && !TrendOrAdj_entity.getRetDimensions().isEmpty())
        trendOrAdj_dimensions = Arrays.asList(TrendOrAdj_entity.getRetDimensions().toLowerCase().split(";", -1));

        String missing_dims = null;
        if( base_entity.getRetDimensions() != null && trendOrAdj_dimensions != null){
        for (String dim : trendOrAdj_dimensions) {
            if(!dimensions.toLowerCase().contains(dim.toLowerCase()))
            {
                missing_dims = dim+",";
            }
           }
        }
        if(missing_dims != null && !missing_dims.isEmpty())
            errorReport.add("All dimensions must be present in Input Dataset and not be de-selected. Missing dimensions : "+StringUtils.substring(missing_dims, 0, missing_dims.length() - 1)+"/Blocking");

//        if(missing_dims != null && !missing_dims.isEmpty())
//            errorReport.add("All dimensions must be present in base table, missing dimensions : "+StringUtils.substring(missing_dims, 0, missing_dims.length() - 1)+"/Blocking");


        if(base_entity != null && TrendOrAdj_entity != null && !base_entity.getRetExposureMethod().trim().equalsIgnoreCase(TrendOrAdj_entity.getRetExposureMethod().trim()))
            errorReport.add("Exposure metric of table should match the exposure metric of the base table. Exposure metric table : "+TrendOrAdj_entity.getRetExposureMethod().trim()+
                    ". Exposure metric base table : "+base_entity.getRetExposureMethod().trim()+"/Warning");

        if(base_entity != null && TrendOrAdj_entity != null && !TrendOrAdj_entity.getCountry().getRcName().equalsIgnoreCase(base_entity.getCountry().getRcName()))
            errorReport.add("Country of table should match the country of the base table. Country table : "+TrendOrAdj_entity.getCountry().getRcName()+
                    ". Country base table : "+base_entity.getCountry().getRcName()+"/Warning");
        if(!isDateOfCommencement && es.checkColumnExistence(TrendOrAdj_entity, "duration_year")) {
            errorReport.add("Table contains duration_year and requires date_of_commencement/Blocking");
        }
        if(!isDateOfBirth && es.checkColumnExistence(TrendOrAdj_entity, "age_attained")) {
            errorReport.add("Table contains age_attained and requires date_of_birth/Blocking");
        }
        String mixAgeControls = mixAgeControls(TrendOrAdj_entity, otherTables);
        if(StringUtils.isNotBlank(mixAgeControls)) {
        	errorReport.add(mixAgeControls);
        }
		if(!isAgeAtcom && es.checkColumnExistence(TrendOrAdj_entity,"insurance_age_attained")) errorReport.add("Table contains insurance_age_attained and requires age_at_commencement, age_at_commencement_definition and date_of_commencement/Blocking");

		return errorReport;
    }
    
	public List<String> checkCalibration(String pathFile,String userId , String studyId ) throws Exception {
		
		List<List<String>> comp = DCFactory.getSchemaChecker().run(pathFile, "calibration",
				SchemaCheckerRegister.EXPTABLE,userId,studyId);
		ControlResults controlResults = DCFactory.getIntegrityChecker().run(null, pathFile, "calibration",
				IntegrityCheckerRegister.EXPTABLE,userId,studyId);

		List<String> errorReport = new ArrayList<>();
		if (comp != null && comp.size() > 0) {
			List<String> compCols = comp.get(1);
			List<String> duplicateCols = comp.get(3);
			if (compCols != null && compCols.size() > 0) {
				errorReport.add("Headers Check :" + String.join(", ", compCols) + " are missing./Blocking");
			}
			if (duplicateCols != null && duplicateCols.size() > 0) {
				errorReport.add("Headers Check :" + String.join(", ", duplicateCols) + " are duplicated./Blocking");
			}
		}
		if(controlResults.getNumber_of_errors()>0) {
			controlResults.getControlResultsList().forEach(result-> {
				if(result.getAffectedColumns() != null && result.getAffectedColumns().size()>0) {
					String msg= "";
					for(AffectedColumn a : result.getAffectedColumns() ) {
						msg = msg + ", " + a.getName();
					}
					errorReport.add("Technical Check : " + result.getControl() + ": for column "+msg.replaceFirst(",", ""));
				}
			});
		}
		
		Stream<String> lines = readFileContent(pathFile);
		List<String> headers = loadFileHeaderLowerCase(pathFile);
		List<String> policyIds = new ArrayList<>();
		List<String> trendIds = new ArrayList<>();
		lines.forEach(line -> {

			String[] array = line.split(";");
			if (headers.contains("policy_table_id")) {
				String policyTableId = array[headers.indexOf("policy_table_id")];
				RefExpectedTableEntity policyTable = es.getByCode(policyTableId);
				if (policyTable == null || !"policy".equalsIgnoreCase(policyTable.getRetType())) {
					policyIds.add(policyTableId);
				}
			}
			if (headers.contains("trend_table_id")) {
				String trendTableId = array[headers.indexOf("trend_table_id")];
				if(StringUtils.isNotBlank(trendTableId)) {
					RefExpectedTableEntity trendTable = es.getByCode(trendTableId);
					if (trendTable == null || !"trend".equalsIgnoreCase(trendTable.getRetType())) {
						trendIds.add(trendTableId);
					}
				}
			}
		
		});
		
		if (policyIds != null && !policyIds.isEmpty()) {
			String msg = "";
			for (String policyId : policyIds.stream().distinct().collect(Collectors.toList())) {
				msg = msg + ", " + policyId;
			}
			errorReport.add("policy table not found for id " + msg.replaceFirst(",", "") + "/Blocking");
		}
		if (trendIds != null && !trendIds.isEmpty()) {
			String msg = "";
			for (String trendId : trendIds.stream().distinct().collect(Collectors.toList())) {
				msg = msg + ", " + trendId;
			}
			errorReport.add("trend table not found for id " + msg.replaceFirst(",", "") + "/Blocking");
		}
		if (headers.indexOf("ibnr_allocation_basis") >-1) {
			String ibnrAllocationBasisControl = calibrationIbnrAllocationBasisControl(pathFile, headers);
			if (StringUtils.isNotBlank(ibnrAllocationBasisControl)) {
				errorReport.add(ibnrAllocationBasisControl);
			} 
		}
		return errorReport;
	}
	
	private String calibrationIbnrAllocationBasisControl(String pathFile, List<String> headers) throws IOException {
		Stream<String> lines = readFileContent(pathFile);
		Map<String, Long> collect = lines
				.map(line -> line.split(";", -1)[headers.indexOf("ibnr_allocation_basis")].toLowerCase().trim())
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		Long yesNb = collect.get("yes");
		if (yesNb == null || yesNb != 1) {
			return "IBNR_allocation_basis must has exactly one line Yes and others No/Blocking";
		}
		Long noNb = collect.get("no");
		Collection<Long> allValues = collect.values();
		long count = 0;
		for (Long values : allValues) {
			count = count + values;
		}
		if(noNb == null && count > 1){
			return "IBNR_allocation_basis must has exactly one line Yes and others No/Blocking";
		}
		if (noNb != null && count != yesNb + noNb) {
			return "IBNR_allocation_basis must has exactly one line Yes and others No/Blocking";
		}
		return null;
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
