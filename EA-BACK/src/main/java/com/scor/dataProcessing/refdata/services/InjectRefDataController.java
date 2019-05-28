package com.scor.dataProcessing.refdata.services;

import java.io.Serializable;

import com.scor.dataProcessing.dataChecker.functionalChecker.Operations.OmegaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InjectRefDataController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6111193989368911928L;
	
	@Autowired
	private InjectRefDataService injectRefDataService;

	@Autowired
	OmegaData omegaData;


	@GetMapping("/retrocessionnaire")
	public void injectRetrocessionnaire() throws Exception {
		injectRefDataService.injectRefDataRetrocessionnaire();
	}

	@GetMapping("/inject")
	public void inject() throws Exception {
		injectRefDataService.injectRefData();
	}

	@GetMapping("/injectClient")
	public void injectClient() throws Exception {
		injectRefDataService.injectProspectClients();
	}
	
	@GetMapping("/injectUserMail")
	public void injectUserMail() throws Exception {
		injectRefDataService.injectUserMail();
	}

	@GetMapping("/joinTreatyWithRefData")
	public void repairjoin()throws Exception{
		injectRefDataService.repairJoin();
	}

	@RequestMapping(value = "/updateOmegaRef", method = RequestMethod.POST)
	public void updateOmegaEntities(){
		omegaData.updateReferencial();
	}


}
