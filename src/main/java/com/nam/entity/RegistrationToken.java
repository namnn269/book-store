package com.nam.entity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "registration_token")
public class RegistrationToken extends UserToken {
	public RegistrationToken(User user) {
		super(user);
	}

	public RegistrationToken() {
		super();
	}


}
