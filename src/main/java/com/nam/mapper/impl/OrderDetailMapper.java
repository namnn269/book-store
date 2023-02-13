package com.nam.mapper.impl;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.nam.dto.OrderDetailDto;
import com.nam.entity.Author;
import com.nam.entity.Book;
import com.nam.entity.OrderDetail;
import com.nam.mapper.IOrderDetailMapper;

@Component
public class OrderDetailMapper implements IOrderDetailMapper{
	
	private NumberFormat numberFormat = new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.GERMAN));

	/* Map từ order detail entity thành order detail DTO */
	@Override
	public OrderDetailDto fromOrderDetailToOrderDetailDto(OrderDetail orderDetail) {
		Book book = orderDetail.getBook();
		OrderDetailDto orderDetailDto = new OrderDetailDto();
		orderDetailDto.setId(orderDetail.getId());
		orderDetailDto.setBookId(book.getId());
		orderDetailDto.setBookTitle(book.getBookTitle());
		orderDetailDto.setCategory(book.getCategory().getCategoryTitle());
		orderDetailDto.setPrice(numberFormat.format(orderDetail.getPrice()));
		orderDetailDto.setImgLink(book.getImgLink());
		orderDetailDto.setAuthors(book.getAuthors()
						.stream()
						.map(Author::getFullname)
						.collect(Collectors.toList()));
		orderDetailDto.setQuantity(orderDetail.getAmount());
		Date date=new Date(orderDetail.getOrder().getOrderDate().getTime());
		orderDetailDto.setDate(date);
		return orderDetailDto;
	}

}
