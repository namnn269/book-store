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
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "discount")
public class Discount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "discount_rate")
	private Byte discountRate;
	
	@Column(name = "discount_amount")
	private double discountAmount;
	
	@Column(name = "max_amount")
	private double maxAmount;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "user_discount",
				joinColumns = @JoinColumn(name="discount_id", referencedColumnName = "id"),
				inverseJoinColumns = @JoinColumn(name="user_id", referencedColumnName = "id"))
	private Collection<User> users = new ArrayList<>();
	
	
}
