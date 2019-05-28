package com.scor.taskManager.taskHandler;

import java.awt.Font;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.mail.MessagingException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.scor.persistance.entities.DataSetEntity;
import com.scor.persistance.entities.EaFilesEntity;
import com.scor.persistance.repositories.DatasetRepository;
import com.scor.persistance.repositories.EaFilesRepository;
import com.scor.persistance.repositories.StudyRepository;
import com.scor.persistance.services.RoleManagerService;
import com.scor.taskManager.models.Email;
import com.scor.taskManager.services.EmailSender;
import com.scor.persistance.entities.RoleExceptionEntity;

@Component
public class DataSetLegalObligations {
	private static final Logger LOGGER = Logger.getLogger(DataSetLegalObligations.class);

	@Autowired
	EaFilesRepository eaFilesRepository;
	@Autowired
	EmailSender emailSender;
	@Autowired
	DatasetRepository datasetRepository;
	@Autowired
	RoleManagerService roleManagerService;
	@Autowired 
	StudyRepository studyRepository ; 

	@Scheduled(cron="0 0 6 * * ?" )
//	@Scheduled(fixedDelay = 10000000, initialDelay = 1000)
	public void dataSetDocumentsDeleter() {
		List<EaFilesEntity> filesToBeDeleted = eaFilesRepository.findByEafDataDeletion(java.sql.Date.valueOf(LocalDate.now()));
		filesToBeDeleted.addAll(eaFilesRepository.findByEafDataDeletion(java.sql.Date.valueOf(LocalDate.now().plusDays(14))));
		filesToBeDeleted.stream().forEach(refFile -> {
//			try {
//				if (Files.deleteIfExists(Paths.get(refFile.getEafLink()))) {
			if (refFile.getEafFileType().equalsIgnoreCase("policy")) {
				DataSetEntity dataSet = datasetRepository.findByDsEventExposureFile(refFile).get(0);
				Integer dsId = dataSet.getDsStId();
				String stCode = studyRepository.findOne(dataSet.getDsStId()).getStCode() ; 
				
				Email email = new Email();
				email.setSubject("Legal obligation : Study ID "+stCode);
				email.setBody("Dear producer(s) of the study <b>" + stCode+ "</b>, <br/><br/>"
						+ "Herewith we would like to remind you that based on the information provided by you or your team the dataset <b>"
						+ dsId + "</b> of <br/>" + "study <b>" + stCode + "</b> needs to be deleted by <b>" + refFile.getEafDataDeletion().toInstant()
					      .atZone(ZoneId.systemDefault())
					      .toLocalDate()
						+ "</b>. <br/><br/>"
						+ "Please note that the data will not be deleted automatically and you need to manually take necessary actions.<br/><br/>"
						+ "In case of any doubt please contact the APEX support team at APEX@scor.com <br/><br/>"
						+ "We thank you for your understanding.<br/>" + "Yours APEX team");
				try {
					List<RoleExceptionEntity> userbyRole = roleManagerService.getRoles(dataSet.getDsStId());
					userbyRole.stream().forEach(user -> {
						if ("producer".equalsIgnoreCase(user.getUreRole())
								&& StringUtils.isNotBlank(user.getUreRuId().getRuMailAdresse())) {
							email.addTo(user.getUreRuId().getRuMailAdresse());
						}
					});

				} catch (Exception e1) {
					e1.printStackTrace();
					return;
				}
				LOGGER.warn("**************************"+ email.getTo()) ; 
				try {
					if (email.getTo().size() > 0)
						emailSender.send(email.getTo().toArray(new String[email.getTo().size()]), email.getSubject(),
								email.getBody());
					LOGGER.warn("************************** done *********************************") ; 
				} catch (MessagingException e) {
					LOGGER.warn(
							"The user with email " + email.getTo() + " was not notified of deleting the file with id "
									+ refFile.getEafId() + "\t" + e.getMessage());
					e.printStackTrace();
				}
			}
//				}
//			} catch (IOException e) {
//				LOGGER.error("The file with path " + refFile.getEafLink() + " cannot be deleted");
//				e.printStackTrace();
//			}
		});
	}

}
