package com.nam.mapper.impl;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nam.dto.AdminBookDto;
import com.nam.dto.DisplayBookDto;
import com.nam.entity.Author;
import com.nam.entity.Book;
import com.nam.entity.Category;
import com.nam.entity.Order;
import com.nam.entity.User;
import com.nam.exception_mesage.ObjectNotFoundException;
import com.nam.mapper.IBookMapper;
import com.nam.repository.IAuthorRepository;
import com.nam.repository.IBookRepository;
import com.nam.repository.ICategoryRepository;
import com.nam.service.IOrderService;
import com.nam.service.IUserService;

@Component
public class BookMapper implements IBookMapper {
	@Autowired
	private  IUserService userService;
	@Autowired
	private  IOrderService orderService;
	@Autowired
	private IBookRepository bookRepo;
	@Autowired
	private ICategoryRepository categoryRepo;
	@Autowired
	private IAuthorRepository authorRepo;
	
	@Override
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
	
	@Override
	public Book fromAdminBookDtoToBook(AdminBookDto bookDto) {
		Book book = new Book();
		if (bookDto.getId() != null) {
			Optional<Book> checkBook = bookRepo.findById(bookDto.getId());
			if (!checkBook.isPresent())
				throw new ObjectNotFoundException("Không tìm thấy sách!");
			book.setId(bookDto.getId());
			book.setImgLink(bookDto.getImgLink());
		}
		book.setBookTitle(bookDto.getBookTitle());
		book.setAmountInStock(bookDto.getAmountInStock());
		book.setDescription(bookDto.getDescription());
		book.setEntryPrice(bookDto.getEntryPrice());
		book.setPrice(bookDto.getPrice());
		book.setPublishingYear(bookDto.getPublishingYear());

		Collection<Author> authors=bookDto.getAuthors_id().stream()
								.map(x-> authorRepo.findById(x).get())
								.collect(Collectors.toList());
		
		Category category=categoryRepo.findById(bookDto.getCategory_id()).get();
		
		book.setAuthors(authors);
		book.setCategory(category);
		return book;
	}
	
	@Override
	public AdminBookDto fromBookToAdminDtoBook(Book book) {
		AdminBookDto bookDto= new AdminBookDto();
		bookDto.setId(book.getId());
		bookDto.setAmountInStock(book.getAmountInStock());
		bookDto.setBookTitle(book.getBookTitle());
		bookDto.setCategory_id(book.getCategory().getId());
		bookDto.setDescription(book.getDescription());
		bookDto.setPrice(book.getPrice());
		bookDto.setEntryPrice(book.getEntryPrice());
		bookDto.setImgLink(book.getImgLink());
		bookDto.setPublishingYear(book.getPublishingYear());
		bookDto.setAuthors_id(book.getAuthors().stream()
						.map(Author::getId).collect(Collectors.toList()));
		return bookDto;
	}

}








