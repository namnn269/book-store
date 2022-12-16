package com.nam.dto;

import java.util.ArrayList;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminBookDto {
	private Long id;

	private String bookTitle;

	private String description;

	private String imgLink;

	private double price;

	private double entryPrice;

	private Integer publishingYear;

	private Long amountInStock;

	private Collection<Long> authors_id = new ArrayList<>();

	private Long category_id;

	private Collection<String> orderDetails = new ArrayList<>();
}
