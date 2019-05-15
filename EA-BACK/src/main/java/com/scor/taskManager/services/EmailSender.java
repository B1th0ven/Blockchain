package com.scor.taskManager.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


@Component
public class EmailSender {
	
	
	@Autowired 
	private  JavaMailSender javaMailSender; 
	@Value("${spring.mail.username}")
	private String from;
	@Value("${mail.default.cc}")
	private String cc;
	
	public  void send ( String[] to,  String subject , String body ) throws MessagingException {
		
		 
		
		MimeMessage message = javaMailSender.createMimeMessage() ; 
		MimeMessageHelper helper ; 
		helper = new MimeMessageHelper(message , true ) ;  
		message.setContent(body, "text/html");
		helper.setFrom(from);
		helper.setSubject(subject);
		helper.setTo(to);
		helper.setCc(this.cc);
		javaMailSender.send(message);
		
	}

}
