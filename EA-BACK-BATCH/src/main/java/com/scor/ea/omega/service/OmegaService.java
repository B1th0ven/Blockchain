package com.scor.ea.omega.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.xml.datatype.DatatypeConfigurationException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class OmegaService {



    private static final Logger LOGGER = Logger.getLogger(OmegaService.class);


    @Value("${batch.frequence}")
    private String batchFrequence;

    @Autowired
    RefCedentNameService refCedentNameService;

    @Autowired
    RefTreatyService refTreatyService;

    //@Scheduled(cron="${batch.date}")
    public void synchronize() throws ParseException, DatatypeConfigurationException {
        Date dateBegin = new Date();
        LOGGER.info("Begin synchronizing Omega WS");
        System.out.println(Long.parseLong(batchFrequence));
        Long timestampBatch = new Date().getTime() - Long.parseLong(batchFrequence);
        Date batch_date = new Date(timestampBatch);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(batch_date);
        injectClient(date);
        injectTreaties(date);
        Date dateEnd = new Date();
        LOGGER.info("End Synchronizing Omega WS in : " + (dateEnd.getTime() - dateBegin.getTime()));
    }

	public void injectTreaties(String date) {
		refTreatyService.injectTreaties(date);
	}

	public void injectClient(String date) {
		refCedentNameService.injectClient(date);
	}
}
