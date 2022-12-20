package com.nam.event;

import org.springframework.context.ApplicationEvent;

import com.nam.entity.User;

import lombok.Getter;

@Getter
public class ResetPasswordEvent extends ApplicationEvent {

	private User user;

	private String url;

	private static final long serialVersionUID = 1L;

	public ResetPasswordEvent(User user, String url) {
		super(user);
		this.user = user;
		this.url = url;
	}

	public ResetPasswordEvent(User user) {
		super(user);
		this.user = user;

	}

}
