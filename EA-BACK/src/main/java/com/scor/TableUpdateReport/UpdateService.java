package com.scor.TableUpdateReport;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scor.TableUpdateReport.model.ControlView;
import com.scor.TableUpdateReport.model.NotExecutedList;
import com.scor.TableUpdateReport.model.TechControlList;
import com.scor.dataProcessing.models.*;
import com.scor.persistance.entities.ControlTableEntity;
import com.scor.persistance.entities.DataSetEntity;
import com.scor.persistance.repositories.ControlTableRepository;
import com.scor.persistance.repositories.DatasetRepository;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UpdateService {


    @Autowired
    DatasetRepository datasetRepository ;

    public static final Map<String, ControlView> controlMap ;

    static {
        Map<String, ControlView> aMap = new HashMap<>();
        aMap.put("status at begin current condition & status at end current condition", new ControlView(3,"policy","Warning"));
        aMap.put("date of birth & date of commencement", new ControlView(4,"policy","Warning"));
        aMap.put("risk amount reinsurer & risk amount insurer", new ControlView(5,"policy","Warning"));
        aMap.put("date of event incurred & type of event", new ControlView(6,"policy","Blocking"));
        aMap.put("date of commencement & date of event incurred", new ControlView(7,"policy","Warning"));
        aMap.put("date of begin current condition & date of end current condition", new ControlView(8,"policy","Blocking"));
        aMap.put("date of commencement & benefit end date", new ControlView(9,"policy","Warning"));
        aMap.put("date of commencement = age at commencement - date of birth", new ControlView(10,"policy && product","Warning"));
        aMap.put("date of commencement & date of begin current condition", new ControlView(11,"policy","Warning"));
        aMap.put("overlap check", new ControlView(12,"policy","Blocking"));
        aMap.put("Type of Event & Status at End Current Condition", new ControlView(13,"policy","Warning"));
        aMap.put("exposure termination", new ControlView(14,"policy","Warning"));
        aMap.put("date of event incurred & benefit end date", new ControlView(15,"policy","Warning"));
        aMap.put("date of end current condition & benefit end date", new ControlView(16,"policy","Warning"));
        aMap.put("start of observation period <= benefit end date", new ControlView(17,"policy","Warning"));
        aMap.put("date of end current condition <= end of observation period", new ControlView(18,"policy","Warning"));
        aMap.put("start of observation period <= date of event incurred", new ControlView(19,"policy","Warning"));
        aMap.put("date of event incurred & date of event notified & date of event settled & date of event paid", new ControlView(20,"policy","Warning"));
        aMap.put("claims existence", new ControlView(21,"policy","Warning"));
        aMap.put("exposure end coherent with status", new ControlView(22,"policy","Warning"));
        aMap.put("cover expiry date - date of birth <= cover expiry age", new ControlView(23,"policy","Warning"));
        aMap.put("when death / withdrawal (lump sum), risk amount = event amount", new ControlView(24,"policy","Warning"));
        aMap.put("product start date <= date of commencement <= product end date", new ControlView(25,"policy","Warning"));
        aMap.put("risk amount insurer <= max face amount", new ControlView(26,"policy","Warning"));
        aMap.put("risk amount reinsurer <= max face amount", new ControlView(27,"policy","Warning"));
        aMap.put("min face amount <= risk amount insurer", new ControlView(28,"policy","Warning"));
        aMap.put("min face amount <= risk amount reinsurer", new ControlView(29,"policy","Warning"));
        aMap.put("min age at commencement <= age at commencement <= max age at commencement", new ControlView(30,"policy","Warning"));
        aMap.put("Unique ProductID Control", new ControlView(31,"product","Blocking"));
        aMap.put("Product Id doesn\\'t exists in product file", new ControlView(32,"policy","Blocking"));
        aMap.put("Date of Event = Date of End Current Condition", new ControlView(33,"policy","Blocking"));
        aMap.put("Missing Values Check", new ControlView(34,"policy","Warning"));
        aMap.put("Variable dependency", new ControlView(35,"policy","Blocking"));
        aMap.put("Events should be coherent with the main risk type and the acceleration risk type", new ControlView(36,"policy","Warning"));
        aMap.put("Product file information should match study metadata", new ControlView(37,"product","Warning"));
        aMap.put("Missing Values Check_2", new ControlView(38,"policy","Blocking"));
        aMap.put("Event Amount Reinsurer <= Event Amount Insurer", new ControlView(39,"policy","Warning"));
        aMap.put("Consistent date of last medical selection", new ControlView(40,"policy","Warning"));
        aMap.put("status date space", new ControlView(41,"policy","Warning"));
        aMap.put("Coherence of Benefit Max Age with policy file", new ControlView(42,"policy && product","Warning"));
        aMap.put("Incidence_Death XOR Incidence/Death", new ControlView(43,"policy","Blocking"));
        aMap.put("Date of Event Paid => Date of Event Incurred + Deferred Period", new ControlView(44,"policy","Warning"));
        aMap.put("Waiting Period", new ControlView(45,"policy","Warning"));
        aMap.put("Event Paid <= Claim Payment End", new ControlView(46,"policy","Warning"));
        aMap.put("Status at benefit end / claim payment end", new ControlView(47,"policy","Warning"));
        aMap.put("Variables consistency", new ControlView(51,"policy && product","Warning"));
        aMap.put("Acceleration Risk Amount and Risk Amount", new ControlView(52,"policy","Blocking"));

        controlMap = aMap;

    }


    @Autowired
    ControlTableRepository controlTableRepository;

    public void updateControls(int id) {
        DataSetEntity dataSetEntity = datasetRepository.findByDsId(id);

        Map<String,ControlView> myMap = new HashedMap<>(controlMap);
        Gson gson = new GsonBuilder().create();
        NotExecutedList notExecutedList = new NotExecutedList();
        TechControlList techControlList = new TechControlList();
        ControlResults functionControlReportdto = null ;
        Set<String> sucessfulControls = myMap.keySet();
        System.out.println("Current id =  "  + id);
        if(dataSetEntity != null){

            String dsNotExecutedReportString =  dataSetEntity.getDsNotExecuted();
            if (StringUtils.isNotBlank(dsNotExecutedReportString)&& !"null".equalsIgnoreCase(dsNotExecutedReportString) ){
                notExecutedList =   gson.fromJson("{ \"notExecutedDtos\":" + dsNotExecutedReportString +"}", notExecutedList.getClass());
                for (NotExecutedDto notExecutedDto : notExecutedList.getNotExecutedDtos()){
                    String controlName = notExecutedDto.getControl();

                    ControlView controlView = controlMap.get(controlName);
                    //TODO persist in REPORT
                    if (controlView != null){
                        ControlTableEntity controlTableEntity = new ControlTableEntity();


                        controlTableEntity.setCtFile(controlView.getControledFile());
                        controlTableEntity.setCtMessage("Not Executed Control");
                        controlTableEntity.setCtName(controlName);
                        controlTableEntity.setCtCategory(controlView.getCategory());
                        controlTableEntity.setCtNumber(controlView.getControlNumber());
                        controlTableEntity.setDsId(id);

                        controlTableRepository.save(controlTableEntity);
                        sucessfulControls.remove(controlName);
                    }


                }
            }

            String dsTechControlReportString =  dataSetEntity.getDsTechReport();
            if(StringUtils.isNotBlank(dsTechControlReportString) && !"null".equalsIgnoreCase(dsTechControlReportString) ){
                techControlList =   gson.fromJson("{ \"techControlResults\":"+dsTechControlReportString+"}", techControlList.getClass());
                TechControlResults policyTechControlResults = techControlList.getTechControlResults()[0] ;
                if(policyTechControlResults.getNumber_of_errors() > 0 ){

                    String controlName = "Format Control";
                    ControlView controlView = controlMap.get(controlName);
                    if (controlView != null){
                        ControlTableEntity controlTableEntity = new ControlTableEntity();
                        controlTableEntity.setCtErrorNumber(policyTechControlResults.getNumber_of_errors());
                        controlTableEntity.setCtFile("Policy");
                        controlTableEntity.setCtMessage("Technical Control");
                        controlTableEntity.setCtName(controlName);
                        controlTableEntity.setCtCategory(controlView.getCategory());
                        controlTableEntity.setCtNumber(1);
                        controlTableEntity.setDsId(id);

                        controlTableRepository.save(controlTableEntity);
                    }

                }


                TechControlResults productTechControlResults = techControlList.getTechControlResults()[1] ;
                if(productTechControlResults.getNumber_of_errors() > 0 ){



                    String controlName = "Format Control";
                    ControlView controlView = controlMap.get(controlName);
                    if (controlView != null){
                        ControlTableEntity controlTableEntity = new ControlTableEntity();
                        controlTableEntity.setCtErrorNumber(policyTechControlResults.getNumber_of_errors());
                        controlTableEntity.setCtFile("Product");
                        controlTableEntity.setCtMessage("Technical Control");
                        controlTableEntity.setCtName(controlName);
                        controlTableEntity.setCtCategory(controlView.getCategory());
                        controlTableEntity.setCtNumber(2);
                        controlTableEntity.setDsId(id);

                        controlTableRepository.save(controlTableEntity);
                    }

                }


            }

            String dsFuncControlReportString =  dataSetEntity.getDsFuncReport();
            if(StringUtils.isNotBlank(dsFuncControlReportString) &&  !"null".equalsIgnoreCase(dsFuncControlReportString) ){
                functionControlReportdto =   gson.fromJson(dsFuncControlReportString, ControlResults.class);
                for(ControlResult controlResult : functionControlReportdto.getControlResultsList()){
                    String controlName = controlResult.getControl();
                    if(sucessfulControls.contains(controlName)){
                        //TODO PERSIST IN REPORT
                        ControlTableEntity controlTableEntity = new ControlTableEntity();
                        ControlView controlView = controlMap.get(controlName);

                        if (controlView != null) {
                            long errorNumber = 0L ;
                            for (AffectedColumn affectedColumn : controlResult.getAffectedColumns()){
                                errorNumber += affectedColumn.getErrorsNumber();
                            }
                            if (errorNumber > 0 ){
                                controlTableEntity.setCtErrorNumber(errorNumber);
                                controlTableEntity.setCtFile(controlView.getControledFile());
                                controlTableEntity.setCtMessage("Functional Control");
                                controlTableEntity.setCtName(controlName);
                                controlTableEntity.setCtCategory(controlView.getCategory());
                                controlTableEntity.setCtNumber(controlView.getControlNumber());
                                controlTableEntity.setDsId(id);

                                controlTableRepository.save(controlTableEntity);
                                sucessfulControls.remove(controlName);
                            }
                        }


                    }
                    else {
                        List<AffectedColumn> affectedColumns = controlResult.getAffectedColumns();
                        for (AffectedColumn affectedColumn : affectedColumns){
                            String affectedColumnName = affectedColumn.getName();
                            if(affectedColumn.getErrorsNumber() > 0 && sucessfulControls.contains(affectedColumnName)) {

                                //TODO  PERSIST REPORT
                                ControlTableEntity controlTableEntity = new ControlTableEntity();
                                ControlView controlView = controlMap.get(affectedColumnName);

                                if (controlView != null){
                                    controlTableEntity.setCtErrorNumber((long)affectedColumn.getErrorsNumber());
                                    controlTableEntity.setCtFile(controlView.getControledFile());
                                    controlTableEntity.setCtMessage("Function Control");
                                    controlTableEntity.setCtName(controlName);
                                    controlTableEntity.setCtCategory(controlView.getCategory());
                                    controlTableEntity.setCtNumber(controlView.getControlNumber());
                                    controlTableEntity.setDsId(id);

                                    controlTableRepository.save(controlTableEntity);
                                    sucessfulControls.remove(controlName);
                                }

                            }

                        }
                    }

                }
            }
            //TODO PERSIST DONE CONTROLS
            if( (StringUtils.isNotBlank(dsFuncControlReportString)|| !"null".equalsIgnoreCase(dsFuncControlReportString)) && (StringUtils.isNotBlank(dsNotExecutedReportString)|| !"null".equalsIgnoreCase(dsFuncControlReportString))  && (StringUtils.isNotBlank(dsTechControlReportString)|| !"null".equalsIgnoreCase(dsFuncControlReportString) )){
                for (String sucessContorl : sucessfulControls){
                    ControlTableEntity controlTableEntity = new ControlTableEntity();
                    ControlView controlView = controlMap.get(sucessContorl);


                    controlTableEntity.setCtErrorNumber(0L);
                    controlTableEntity.setCtFile(controlView.getControledFile());
                    controlTableEntity.setCtMessage("Success");
                    controlTableEntity.setCtName(sucessContorl);
                    controlTableEntity.setCtCategory(controlView.getCategory());
                    controlTableEntity.setCtNumber(controlView.getControlNumber());
                    controlTableEntity.setDsId(id);
                    controlTableRepository.save(controlTableEntity);
                }
            }

        }

    }


    public void updateReports() {

        for(DataSetEntity dataSetEntity :  datasetRepository.findAll()) {
            updateControls(dataSetEntity.getDsId());
        }

    }
}
