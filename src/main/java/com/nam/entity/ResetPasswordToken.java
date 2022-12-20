package com.nam.entity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "reset_password_token")
public class ResetPasswordToken extends UserToken {
	public ResetPasswordToken(User user) {
		super(user);
	}

	public ResetPasswordToken() {
		super();
	}
}
