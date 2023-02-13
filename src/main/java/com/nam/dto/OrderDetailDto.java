package com.nam.dto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDto {
	
	private Long id;
	
	private Long bookId;

	private String bookTitle;
	
	private String category;
	
	private String imgLink;
	
	private String price;
	
	private Long quantity;
	
	private Date date;
	
	private Collection<String> authors = new ArrayList<>();

}
