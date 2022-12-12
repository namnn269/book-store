package com.nam.entity;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "fullname")
	private String fullName;

	@Column(length = 250)
	private String username;

	@Column
	private String password;

	@Column(length = 200)
	private String email;

	@Column
	private boolean enabled = false;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	private Collection<Role> roles=new ArrayList<>();

	@OneToOne(mappedBy = "user")
	private RegistrationToken registrationToken;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private ProfileUser profileUser;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Collection<Order> orders = new ArrayList<>();
	
	@ManyToMany(mappedBy = "users",cascade = CascadeType.ALL)
	private Collection<Discount> discounts=new ArrayList<>(); 

}
