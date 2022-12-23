package com.nam.entity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "reset_password_token")
public class ResetPasswordToken extends UserToken {
	public ResetPasswordToken(User user, long expiration) {
		super(user, expiration);
	}
	
	public ResetPasswordToken() {
		super();
	}
}
