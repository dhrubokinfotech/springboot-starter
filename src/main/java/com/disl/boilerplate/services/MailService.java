package com.disl.boilerplate.services;

import com.disl.boilerplate.config.EmailProperties;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.disl.boilerplate.BoilerplateApplication.logger;

@Service
public class MailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private EmailProperties emailProperties;

	@Async
	public void sendMailToMany(String[] emails, String subject, String text) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(emails);
		msg.setSubject(subject);
		msg.setText(text);
		javaMailSender.send(msg);
	}

	@Async
	public void sendMail(String toMail, String subject, String message) {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
			helper.setText(message, true);
			helper.setTo(toMail);
			helper.setSubject(subject);
			helper.setFrom(emailProperties.getFrom());
			javaMailSender.send(mimeMessage);

			logger.info("Email sent!");
		} catch (Exception ex) {
			logger.info("The email was not sent. Error message: " + ex.getMessage());
		}
	}
}
