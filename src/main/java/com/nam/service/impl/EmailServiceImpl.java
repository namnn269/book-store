package com.nam.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.nam.dto.EmailDto;
import com.nam.service.IEmailService;

@Service
@PropertySource(value = "messages.properties", encoding = "utf-8")
public class EmailServiceImpl implements IEmailService {

	@Autowired
	private JavaMailSender jSender;

	@Autowired
	private Environment env;

	@Value("${email.from}")
	private String mailFrom;

	/* Nhận vào email Dto, gán vào email để thực hiện gửi email */
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
			return env.getProperty("message.send.email.success");
		} catch (MailException m) {
			return env.getProperty("message.send.email.error");
		} catch (MessagingException e) {
			e.printStackTrace();
			return env.getProperty("message.send.email.error");
		}

	}

}
