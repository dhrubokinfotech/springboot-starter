package com.disl.boilerplate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	@Autowired
	private JavaMailSender javaMailSender;

   @Async
   public void sendMail (String email, String subject, String text) {
	   SimpleMailMessage msg = new SimpleMailMessage();
	   msg.setTo(email);
	   msg.setSubject(subject);
	   msg.setText(text);
	   javaMailSender.send(msg);
   }
   
	@Async
	public void sendMailToMany(String[] emails, String subject, String text) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(emails);
		msg.setSubject(subject);
		msg.setText(text);
		javaMailSender.send(msg);
	}
}
