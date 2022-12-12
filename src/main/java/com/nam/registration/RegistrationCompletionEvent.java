package com.nam.registration;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.nam.entity.User;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class RegistrationCompletionEvent extends ApplicationEvent {

	private Locale locale;
	private String url;
	private User user;

	public RegistrationCompletionEvent(User user, Locale locale, String url) {
		super(user);
		this.locale = locale;
		this.url = url;
		this.user = user;
	}

	public RegistrationCompletionEvent(User user) {
		super(user);
		this.user = user;
	}

}
