package com.scor.dataProcessing.refdata.services;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InjectRefDataController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6111193989368911928L;
	
	@Autowired
	private InjectRefDataService injectRefDataService;
	
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


}
