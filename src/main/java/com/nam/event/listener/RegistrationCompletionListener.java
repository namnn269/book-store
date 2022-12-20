package com.nam.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.nam.dto.EmailDto;
import com.nam.entity.RegistrationToken;
import com.nam.entity.User;
import com.nam.event.RegistrationCompletionEvent;
import com.nam.repository.IRegistraionTokenRepository;
import com.nam.service.IEmailService;

@Component
public class RegistrationCompletionListener implements ApplicationListener<RegistrationCompletionEvent> {

	@Autowired
	private IRegistraionTokenRepository tokenRepo;

	@Autowired
	private IEmailService emailService;

	@Override
	public void onApplicationEvent(RegistrationCompletionEvent event) {
		User user = event.getUser();
		RegistrationToken token = user.getRegistrationToken();
		if (token != null) // TH token quá hạn => reset token
			token.resetToken();
		else
			token = new RegistrationToken(user); // TH tạo mới cho token

		tokenRepo.save(token);

		EmailDto emailDto = new EmailDto();
		String content = "<h2>Click link dưới để xác nhận đăng ký <h2>";
		content += "<br> <a href='" + event.getUrl() + "/register/email-confirm?token=" + token.getToken()
				+ "'>Link xác nhận đăng ký</a> <span>(Hết hạn sau "+ token.getEXPIRATION()/60 +" phút)</span>";
		emailDto.setTo(user.getEmail());
		emailDto.setSubject("Xác nhận đăng ký!");
		emailDto.setContent(content);
		emailService.sendConfirmationEmail(emailDto);
	}
}
