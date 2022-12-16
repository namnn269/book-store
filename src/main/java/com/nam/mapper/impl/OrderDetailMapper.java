package com.nam.mapper.impl;

import java.sql.Date;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.nam.dto.PurchasedOrderDetailDto;
import com.nam.entity.Author;
import com.nam.entity.Book;
import com.nam.entity.OrderDetail;
import com.nam.mapper.IOrderDetailMapper;

@Component
public class OrderDetailMapper implements IOrderDetailMapper{

	@Override
	public PurchasedOrderDetailDto fromOrderDetailToPurchasedBookDto(OrderDetail orderDetail) {
		Book book = orderDetail.getBook();
		PurchasedOrderDetailDto orderDetailDto = new PurchasedOrderDetailDto();
		orderDetailDto.setId(book.getId());
		orderDetailDto.setBookTitle(book.getBookTitle());
		orderDetailDto.setCategory(book.getCategory().getCategoryTitle());
		orderDetailDto.setPrice(book.getPrice());
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
