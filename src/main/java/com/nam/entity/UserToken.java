package com.nam.entity;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@MappedSuperclass
public class UserToken {
	
	private Long EXPIRATION = 60l; // seconds

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String token;

	@Column(name = "expiration_date")
	private Timestamp expirationDate;

	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	public UserToken(User user, long expiration) {
		this.EXPIRATION = expiration;
		this.user = user;
		this.token = UUID.randomUUID().toString();
		this.expirationDate = getNextExpirationDate();
	}

	public UserToken() {
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
