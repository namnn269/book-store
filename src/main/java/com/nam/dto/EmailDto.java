package com.nam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailDto  {
	private String to;
	private String subject;
	private String content;
	private String attachment;
}
