package com.nam.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.nam.dto.EmailDto;
import com.nam.service.IEmailService;

@Service
public class EmailServiceImpl implements IEmailService {

	@Autowired
	private JavaMailSender jSender;

//	@Autowired
//	private Environment env;

	@Value("${email.from}")
	private String mailFrom;

	@Override
	public String sendConfirmationEmail(EmailDto email) {
		MimeMessage mimeMailMessage = jSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, true);
//			helper.setFrom(env.getProperty("email.from"));
			helper.setFrom(mailFrom);
			helper.setTo(email.getTo());
			helper.setSubject(email.getSubject());
			helper.setText(email.getContent(), true);
			jSender.send(mimeMailMessage);
			return "Email sended successfully";
		} catch (MailException m) {
			return "Error when sending email 1";
		} catch (MessagingException e) {
			e.printStackTrace();
			return "Error when sending email 2";
		}

	}

}
