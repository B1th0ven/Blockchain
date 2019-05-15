package com.scor.ea.omega.configuration;

import com.scor.ref_treaty.schemas._1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;

import com.scor.ea.omega.service.LoggingInterceptors;
import com.scor.ea.omega.ws_consumer.ScorConsumer;
import com.scor.ea.util.EaUtilities;
import com.scor.ref_client.schemas._1.FindClientByIdRequest;
import com.scor.ref_client.schemas._1.FindClientByIdResponse;
import com.scor.ref_client.schemas._1.FindClientByLastModifiedDateRequest;
import com.scor.ref_client.schemas._1.FindClientByLastModifiedDateResponse;

@SuppressWarnings("deprecation")
@Configuration
public class OmegaWebServiceConfiguration {

	@Value("${ws.omega.password}")
	private String WS_PASSWORD;

	@Value("${ws.omega.username}")
	private String WS_USERNAME;

	@Value("${ws.omega.treaty.endpoint}")
	private String TREATY_URI;

	@Value("${ws.omega.client.endpoint}")
	private String CLIENT_URI;

	@Autowired
	private LoggingInterceptors loggingInterceptors;

	/**
	 * Marshaller bean for ScorClient WS
	 * 
	 * @return Jaxb2Marshaller
	 */
	@Bean
	public Jaxb2Marshaller marshallerClient() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath(FindClientByIdRequest.class.getPackage().getName());
		return marshaller;
	}

	@Bean
	public WebServiceTemplate webServiceTemplateClient() {
		WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
		webServiceTemplate.setMarshaller(this.marshallerClient());
		webServiceTemplate.setUnmarshaller(this.marshallerClient());
		webServiceTemplate.setDefaultUri(CLIENT_URI);
		ClientInterceptor[] interceptors = { securityInterceptor(), loggingInterceptors };
		webServiceTemplate.setInterceptors(interceptors);
		return webServiceTemplate;
	}

	/**
	 * Marshaller bean for ScorTreaty WS
	 * 
	 * @return Jaxb2Marshaller
	 */
	@Bean
	public Jaxb2Marshaller marshallerTreaty() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		//marshaller.setContextPath(FindTtyByLastModifiedDateRequest.class.getPackage().getName());
        marshaller.setPackagesToScan("com.scor.ref_treaty.schemas._1");
		return marshaller;
	}

	@Bean
	public WebServiceTemplate webServiceTemplateTreaty() {
		WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
		webServiceTemplate.setMarshaller(this.marshallerTreaty());
		webServiceTemplate.setUnmarshaller(this.marshallerTreaty());
		webServiceTemplate.setDefaultUri(TREATY_URI);
		ClientInterceptor[] interceptors = { securityInterceptor(), loggingInterceptors };
		webServiceTemplate.setInterceptors(interceptors);
		return webServiceTemplate;
	}

	@SuppressWarnings("deprecation")
	@Bean
	public Wss4jSecurityInterceptor securityInterceptor() {
		Wss4jSecurityInterceptor securityInterceptor = new Wss4jSecurityInterceptor();
		securityInterceptor.setSecurementActions(EaUtilities.USERNAME_TOKEN);
		securityInterceptor.setSecurementUsername(WS_USERNAME);
		securityInterceptor.setSecurementPassword(WS_PASSWORD);
		securityInterceptor.setSecurementPasswordType(EaUtilities.PASSWORD_DIGEST);
		securityInterceptor.setSecurementUsernameTokenElements(EaUtilities.NONCE_CREATED);

		return securityInterceptor;

	}

	@Bean
	public ScorConsumer<FindClientByIdRequest, FindClientByIdResponse> scorConsumerFindClientById() {
		ScorConsumer<FindClientByIdRequest, FindClientByIdResponse> scorConsumer = new ScorConsumer<>();
		scorConsumer.setWebServiceTemplate(this.webServiceTemplateClient());
		return scorConsumer;
	}

	@Bean
	public ScorConsumer<FindClientByLastModifiedDateRequest, FindClientByLastModifiedDateResponse> scorConsumerFindClientByLastModifiedDate() {
		ScorConsumer<FindClientByLastModifiedDateRequest, FindClientByLastModifiedDateResponse> scorConsumer = new ScorConsumer<>();
		scorConsumer.setWebServiceTemplate(this.webServiceTemplateClient());
		return scorConsumer;
	}

	@Bean
	public ScorConsumer<FindTtyByLastModifiedDateRequest, FindTtyByLastModifiedDateResponse> scorConsumerFindTtyByLastModifiedDate() {
		ScorConsumer<FindTtyByLastModifiedDateRequest, FindTtyByLastModifiedDateResponse> scorConsumer = new ScorConsumer<>();
		scorConsumer.setWebServiceTemplate(this.webServiceTemplateTreaty());
		return scorConsumer;
	}

	@Bean
	public ScorConsumer<FindTtyCountryPerilByIdRequest, FindTtyCountryPerilByIdResponse> scorConsumerFindTtyCountryPerilById() {
		ScorConsumer<FindTtyCountryPerilByIdRequest, FindTtyCountryPerilByIdResponse> scorConsumer = new ScorConsumer<>();
		scorConsumer.setWebServiceTemplate(this.webServiceTemplateTreaty());
		return scorConsumer;
	}

	@Bean
	public ScorConsumer<FindTtyGuaranteeByIdRequest, FindTtyGuaranteeByIdResponse> scorConsumerFindTtyGuaranteeById() {
		ScorConsumer<FindTtyGuaranteeByIdRequest, FindTtyGuaranteeByIdResponse> scorConsumer = new ScorConsumer<>();
		scorConsumer.setWebServiceTemplate(this.webServiceTemplateTreaty());
		return scorConsumer;
	}

	@Bean
	public ScorConsumer<FindTtyHeaderByIdRequest, FindTtyHeaderByIdResponse> scorConsumerFindTtyHeaderById() {
		ScorConsumer<FindTtyHeaderByIdRequest, FindTtyHeaderByIdResponse> scorConsumer = new ScorConsumer<>();
		scorConsumer.setWebServiceTemplate(this.webServiceTemplateTreaty());
		return scorConsumer;
	}

	@Bean
	public ScorConsumer<FindTtyReinstatementByIdRequest, FindTtyReinstatementByIdResponse> scorConsumerFindTtyReinstatementById() {
		ScorConsumer<FindTtyReinstatementByIdRequest, FindTtyReinstatementByIdResponse> scorConsumer = new ScorConsumer<>();
		scorConsumer.setWebServiceTemplate(this.webServiceTemplateTreaty());
		return scorConsumer;
	}

	@Bean
	public ScorConsumer<FindTtySectionLabelListByIdRequest, FindTtySectionLabelListByIdResponse> scorConsumerFindTtySectionLabelListById() {
		ScorConsumer<FindTtySectionLabelListByIdRequest, FindTtySectionLabelListByIdResponse> scorConsumer = new ScorConsumer<>();
		scorConsumer.setWebServiceTemplate(this.webServiceTemplateTreaty());
		return scorConsumer;
	}

	@Bean
	public ScorConsumer<FindTtySectionListByIdRequest, FindTtySectionListByIdResponse> scorConsumerFindTtySectionListById() {
		ScorConsumer<FindTtySectionListByIdRequest, FindTtySectionListByIdResponse> scorConsumer = new ScorConsumer<>();
		scorConsumer.setWebServiceTemplate(this.webServiceTemplateTreaty());
		return scorConsumer;
	}

	@Bean
	public ScorConsumer<FindTtyTermCondByIdRequest, FindTtyTermCondByIdResponse> scorConsumerFindTtyTermCondById() {
		ScorConsumer<FindTtyTermCondByIdRequest, FindTtyTermCondByIdResponse> scorConsumer = new ScorConsumer<>();
		scorConsumer.setWebServiceTemplate(this.webServiceTemplateTreaty());
		return scorConsumer;
	}

	@Bean
	public ScorConsumer<FindTtyListByIdRequest, FindTtyListByIdResponse> scorConsumerFindTtyListById() {
		ScorConsumer<FindTtyListByIdRequest, FindTtyListByIdResponse> scorConsumer = new ScorConsumer<>();
		scorConsumer.setWebServiceTemplate(this.webServiceTemplateTreaty());
		return scorConsumer;
	}

}
