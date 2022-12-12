package com.nam.mapper;

import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nam.dto.DisplayBookDto;
import com.nam.entity.Author;
import com.nam.entity.Book;
import com.nam.entity.Order;
import com.nam.entity.User;
import com.nam.service.IOrderService;
import com.nam.service.IUserService;

@Component
public class MapperBook {
	@Autowired
	private  IUserService userService;
	@Autowired
	private  IOrderService orderService;
	
	public DisplayBookDto fromBookToDisplayBookDto(Book book) {
		DisplayBookDto bookDto = new DisplayBookDto();
		bookDto.setId(book.getId());
		bookDto.setAmountInStock(book.getAmountInStock());
		bookDto.setBookTitle(book.getBookTitle());
		bookDto.setCategory(book.getCategory().getCategoryTitle());
		bookDto.setDescription(book.getDescription());
		bookDto.setPrice(book.getPrice());
		bookDto.setEntryPrice(book.getEntryPrice());
		bookDto.setImgLink(book.getImgLink());
		bookDto.setPublishingYear(book.getPublishingYear());
		bookDto.setAuthors(book.getAuthors().stream()
				.map(Author::getFullname).collect(Collectors.toList()));
		bookDto.setAuthorAndDescription(
				book.getAuthors().stream()
				.map(x->{return x.getFullname()+" :"+x.getDescription();})
				.collect(Collectors.toList()));
		try {
			User user=userService.getCurrentLoggedInUser();
				Order order= orderService.findCurrentOrder(user);
				bookDto.setAmountInCart(
						order.getOrderDetails()
						.stream()
						.filter(x -> {return Objects.equals(x.getBook().getId(), book.getId());})
						.findFirst()
						.get()
						.getAmount());
		} catch (Exception e) {
			bookDto.setAmountInCart(0l);
		}
		return bookDto;
	}
	

}








