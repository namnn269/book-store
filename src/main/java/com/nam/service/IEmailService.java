package com.nam.service;

import com.nam.dto.EmailDto;

public interface IEmailService {
	String sendConfirmationEmail(EmailDto email);
}
