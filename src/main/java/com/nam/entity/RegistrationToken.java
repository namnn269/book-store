package com.nam.entity;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "registration_token")
public class RegistrationToken {
//	private final Long EXPIRATION = 24 * 60 * 60L; // seconds
	private final Long EXPIRATION = 20L; // seconds

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String token;

	@Column(name = "expiration_date")
	private Timestamp expirationDate;

	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	public RegistrationToken() {
		this.token = UUID.randomUUID().toString();
		this.expirationDate = getNextExpirationDate();
	}

	public RegistrationToken(User user) {
		this.user = user;
		this.token = UUID.randomUUID().toString();
		this.expirationDate = getNextExpirationDate();
	}

	public void resetToken() {
		this.token = UUID.randomUUID().toString();
		this.expirationDate = getNextExpirationDate();
	}

	private Timestamp getNextExpirationDate() {
		Long time = Calendar.getInstance().getTimeInMillis();
		return new Timestamp(time + EXPIRATION * 1000);
	}

}
