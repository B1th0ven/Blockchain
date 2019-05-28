package com.scor.dataProcessing.dataChecker.functionalChecker.Controls;

import com.scor.dataProcessing.dataChecker.functionalChecker.Operations.OmegaData;
import com.scor.dataProcessing.models.AffectedColumn;
import com.scor.dataProcessing.models.ControlResult;
import com.scor.persistance.entities.ViewOmegaEntity;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Control53 implements Serializable {

    public Control53() {

    }

    List<ViewOmegaEntity> omegaEntities = OmegaData.viewOmegaEntities;


    public ControlResult control53review(String productFilePath) throws IOException {
        List<AffectedColumn> affectedColumns = new ArrayList<>();
        ControlResult controlResult = new ControlResult();
        controlResult.setControl("Omega consistency");
        FileReader fr = new FileReader(productFilePath);
        BufferedReader br = new BufferedReader(fr);
        int numberOferrors = 0 ;
        String header = br.readLine();
        List<String> names = Arrays.asList(header.toLowerCase().trim().split(";", -1));

        boolean isTreatyNumberOmega = names.contains("treaty_number_omega");
        boolean isClientOmegaCode = names.contains("client_omega_code");
        boolean isClientRiskCarrierName = names.contains("client_risk_carrier_name");
        boolean isCommercialClientName = names.contains("commercial_client_name");
        boolean isClientGroupOmegaCode = names.contains("client_group_omega_code");
        boolean isClientGroup = names.contains("client_group");
        boolean isClientCountry = names.contains("client_country");
        boolean isPortfolioOrigin = names.contains("portfolio_origin");
        boolean isCurrency = names.contains("currency");
        boolean isLegalEntity = names.contains("legal_entity");
        boolean isLegalEntityCode = names.contains("legal_entity_code");

        String row = null;
        while ((row = br.readLine()) != null) {
            boolean found = false ;
            String[] row_arr = row.toLowerCase().trim().split(";", -1);

            for(ViewOmegaEntity viewOmegaEntity :  omegaEntities){
                boolean treaty = isVariableOK(isTreatyNumberOmega,row_arr,names,viewOmegaEntity.getTreatyNumberOmega(),"treaty_number_omega");
                boolean clientOmegaCode = isVariableOK(isClientOmegaCode,row_arr,names,viewOmegaEntity.getClientOmegaId(),"client_omega_code");
                boolean clientRiskCarrierName = isVariableOK(isClientRiskCarrierName,row_arr,names,viewOmegaEntity.getClientRiskCarrierName(),"client_risk_carrier_name");
                boolean commercialClientName = isVariableOK(isCommercialClientName,row_arr,names,viewOmegaEntity.getCommercialClientName(),"commercial_client_name");
                boolean clientGroupOmegaCode = isVariableOK(isClientGroupOmegaCode,row_arr,names,viewOmegaEntity.getParentGroupCode(),"client_group_omega_code");
                boolean clientGroup = isVariableOK(isClientGroup,row_arr,names,viewOmegaEntity.getParentGroupname(),"client_group");
                boolean clientCountry = isVariableOK(isClientCountry,row_arr,names,viewOmegaEntity.getClientCountry(),"client_country");
                boolean portfolioOrigin = isVariableOK(isPortfolioOrigin,row_arr,names,viewOmegaEntity.getPortfolioOrigin(),"portfolio_origin");
                boolean currency = isVariableOK(isCurrency,row_arr,names,viewOmegaEntity.getCurrency(),"currency");
                boolean legalEntity = isVariableOK(isLegalEntity,row_arr,names,viewOmegaEntity.getLegalEntity(),"legal_entity");
                boolean legalEntityCode = isVariableOK(isLegalEntityCode,row_arr,names,viewOmegaEntity.getLegalEntityCode(),"legal_entity_code");
                if( treaty && clientOmegaCode && clientGroupOmegaCode && clientRiskCarrierName && clientGroup && clientCountry && portfolioOrigin && currency && legalEntity && legalEntityCode )
                {
                    found = true ;
                    break;
                }
            }
            if(found == false){
                //TODO ENTITY NOT FOUND
                affectedColumns.add(new AffectedColumn("Omega consistency",1, new ArrayList<>(Arrays.asList(String.join(";",row)))));
                numberOferrors ++ ;
            }
        }
        if(numberOferrors > 0 ){
            return new ControlResult("Omega consistency",affectedColumns);
        }
        return null;
    }

    private boolean isVariableOK(boolean isVariable,String[] row_arr , List<String> names ,String EntityValue ,String FieldName){
        if (isVariable){
            if(    StringUtils.isNotBlank(row_arr[names.indexOf(FieldName)]) &&   row_arr[names.indexOf(FieldName)].equalsIgnoreCase(EntityValue))  {
                return true ;
            }
            else return false ;
        }
        return true;
    }




}
