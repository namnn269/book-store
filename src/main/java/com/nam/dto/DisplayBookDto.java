package com.nam.dto;

import java.util.ArrayList;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisplayBookDto {
	private Long id;

	private String bookTitle;

	private String description;

	private String imgLink;

	private String price;

	private double entryPrice;

	private Integer publishingYear;

	private Long amountInStock;

	private Long amountInCart;
	
	private Collection<String> authors = new ArrayList<>();
	
	private Collection<String> authorAndDescription=new ArrayList<>();

	private String category;

	private Collection<String> orderDetails = new ArrayList<>();
}
