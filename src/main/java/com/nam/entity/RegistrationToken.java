package com.nam.entity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "registration_token")
public class RegistrationToken extends UserToken {
	public RegistrationToken(User user, long expiration) {
		super(user, expiration);
	}

	public RegistrationToken() {
		super();
	}
	

}
