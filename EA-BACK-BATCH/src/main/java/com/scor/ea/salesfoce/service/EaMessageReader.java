package com.scor.ea.salesfoce.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.scor.ea.salesfoce.entities.RefDataEntity;
import com.scor.ea.salesfoce.repository.RefDataRepository;
import com.scor.ea.util.EaUtilities;

@Service
public class EaMessageReader {
	private static final Logger LOGGER = Logger.getLogger(EaMessageReader.class);
	private static final String JMS_FILE = "${jms.salesforce.file}";

	@Value(JMS_FILE)
	private String jmsFile;

	@Autowired
	private RefDataRepository refDataRepository;

	public void doAction(TextMessage message) throws JMSException {
		generateFileCsv(message);
		RefDataEntity deals = extractRefDeals(message);
		List<RefDataEntity> foundDeals = refDataRepository.findByTypeAndCodeAndName(deals.getType(), deals.getCode(),
				deals.getName());
		if(foundDeals != null && foundDeals.size()>0) {
			LOGGER.warn("Deal : " + foundDeals.get(0).getCode() + " found in RefData");
			return;
		}
		deals = refDataRepository.save(deals);
	}

	private RefDataEntity extractRefDeals(TextMessage message) throws JMSException {
		String[] messageArray = message.getText().split(";");

		RefDataEntity deals = new RefDataEntity();
		deals.setType("deals");
		deals.setCode(messageArray[0]);
		deals.setName(messageArray[1]);

		return deals;
	}

	private void generateFileCsv(TextMessage message) throws JMSException {
		String content = "";
		String fileSuffix = EaUtilities.getDate();
		String pathname = jmsFile + "_" + fileSuffix + ".csv";
		File fileDestination = new File(pathname);

		Path path = Paths.get(pathname);
		if (!Files.exists(path)) {
			try {
				fileDestination.createNewFile();
				content = "Deal ID;Deal number;Deal Description;Omega Ref. #;Account Name (*)\n";
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		content = content + message.getText() + "\n";
		LOGGER.info("Message Text : " + message.getText());

		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileDestination, true);
			bw = new BufferedWriter(fw);
			bw.write(content);

			LOGGER.info("Done saving message : '" + message.getText() + "' to file : " + pathname);

		} catch (IOException e) {

			e.printStackTrace();

		} catch (JMSException e) {
			e.printStackTrace();
		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}
}
