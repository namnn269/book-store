package com.nam.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.nam.dto.EmailDto;
import com.nam.entity.ResetPasswordToken;
import com.nam.entity.User;
import com.nam.event.ResetPasswordEvent;
import com.nam.repository.IResetPasswordTokenRepository;
import com.nam.service.IEmailService;

@Component
public class ResetPasswordListener implements ApplicationListener<ResetPasswordEvent> {

	@Autowired
	IEmailService emailService;
	@Autowired
	IResetPasswordTokenRepository resetPasswordTokenRepo;
	@Autowired
	private Environment env;

	@Override
	public void onApplicationEvent(ResetPasswordEvent event) {
		Long expiration = Long.parseLong(env.getProperty("resetpassword.expiration"));
		User user = event.getUser();
		ResetPasswordToken token = user.getPasswordToken();
		if (token == null)
			token = new ResetPasswordToken(user,expiration);
		else
			token.resetToken();

		resetPasswordTokenRepo.save(token);

		EmailDto emailDto = new EmailDto();
		emailDto.setSubject("Xác nhận cấp lại mật khẩu cho username: " + user.getUsername());
		emailDto.setTo(user.getEmail());

		String content = "<h2>Click link dưới để xác nhận cấp lại mật khẩu <h2>";
		content += "<br> <a href='" + event.getUrl() + "/trang-chu/email-confirm-reset-password?token=" + token.getToken()
		+ "'>Link xác nhận cấp lại mật khẩu</a> <span>Hết hạn sau " + token.getEXPIRATION()/60 + " phút </span>";
		emailDto.setContent(content);
		emailService.sendConfirmationEmail(emailDto);

	}

}












