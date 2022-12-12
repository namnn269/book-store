package com.nam.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nam.dto.DisplayBookDto;
import com.nam.dto.NewBookDto;
import com.nam.entity.Author;
import com.nam.entity.Book;
import com.nam.entity.Category;
import com.nam.exception_mesage.Message;
import com.nam.exception_mesage.ObjectNotFoundException;
import com.nam.mapper.MapperBook;
import com.nam.repository.IAuthorRepository;
import com.nam.repository.IBookRepository;
import com.nam.repository.ICategoryRepository;
import com.nam.service.IBookService;

@Service
public class BookServiceImpl implements IBookService {
	@Autowired
	private IBookRepository bookRepo;
	
	@Autowired
	private ICategoryRepository categoryRepo;
	
	@Autowired
	private IAuthorRepository authorRepo;
	
	@Autowired
	private MapperBook mapperBook;

	@Override
	public Optional<Book> findById(Long id) {
		return bookRepo.findById(id);
	}

	@Override
	public List<Book> findAll() {
		return bookRepo.findAll();
	}

	@Override
	public Message save(NewBookDto bookDto) {
		Book book = new Book();
		if (bookDto.getId() != null) {
			Optional<Book> checkBook = bookRepo.findById(bookDto.getId());
			if (!checkBook.isPresent())
				throw new ObjectNotFoundException("Không tìm thấy sách!");
			book.setId(bookDto.getId());
		}
		book.setBookTitle(bookDto.getBookTitle());
		book.setAmountInStock(bookDto.getAmountInStock());
		book.setDescription(bookDto.getDescription());
		book.setEntryPrice(bookDto.getEntryPrice());
		book.setImgLink(bookDto.getImgLink());
		book.setPrice(bookDto.getPrice());
		book.setPublishingYear(bookDto.getPublishingYear());

		Collection<Author> authors=bookDto.getAuthors_id().stream()
								.map(x-> authorRepo.findById(x).get())
								.collect(Collectors.toList());
		
		Category category=categoryRepo.findById(bookDto.getCategory_id()).get();
		
		book.setAuthors(authors);
		book.setCategory(category);
		bookRepo.save(book);
		return new Message("Thành công! ID: "+book.getId());
	}

	@Override
	public List<DisplayBookDto> findAll(int pageNo, int pageSize, String sortBy) {
		
		Direction direction= Direction.ASC;
		if (sortBy.equals("price-down")) {
			sortBy = "price";
			direction = Direction.DESC;
		} else if(sortBy.equals("price-up")) {
			sortBy = "price";
		}
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction,sortBy));
		Page<Book> page = bookRepo.findAll(pageable);
		if (!page.hasContent())
			return Collections.emptyList();

		return page.getContent().stream().map(book -> {
			return mapperBook.fromBookToDisplayBookDto(book);
		}).collect(Collectors.toList());
	}

	@Override
	public Page<Book> getPageBook(int pageNo, int pageSize, String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		return bookRepo.findAll(pageable);
	}

	@Override
	public Message delete(Long id) {
		Optional<Book> opBook= bookRepo.findById(id);
		if(opBook.isPresent()) {
			Book delBook=opBook.get();
			delBook.getCategory().getBooks().remove(delBook);
			delBook.setAuthors(null);
			bookRepo.delete(delBook);
			return new Message("Xóa sách thành công! ID: "+id);
		} else {
			throw new ObjectNotFoundException("Không tìm thấy sách");
		}
	}

	@Override
	public NewBookDto getUpdateBook(Long id) {
		Optional<Book> opBook = bookRepo.findById(id);
		if(!opBook.isPresent())throw new ObjectNotFoundException("Không tìm thấy sách!");
		Book book=opBook.get();
		NewBookDto bookDto= new NewBookDto();
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

	@Override
	public DisplayBookDto findBookDetailById(Long id) {
		Optional<Book> opBook= bookRepo.findById(id);
		if(!opBook.isPresent()) throw new ObjectNotFoundException("Không tìm thấy sách");
		Book book=opBook.get();
		DisplayBookDto bookDto=mapperBook.fromBookToDisplayBookDto(book);
		return bookDto;
	}

	@Override
	public List<DisplayBookDto> findAllBySearchKey(String sortBy, String searchKey) {
		Direction direction= Direction.ASC;
		if (sortBy.equals("price-down")) {
			sortBy = "price";
			direction = Direction.DESC;
		} else if(sortBy.equals("price-up")) {
			sortBy = "price";
		}
		List<Book> books =bookRepo.findAllBySortAndSearch(searchKey,Sort.by(direction,sortBy));
		List<DisplayBookDto> bookDtos = new ArrayList<>();
		bookDtos = books.stream().map(x->mapperBook.fromBookToDisplayBookDto(x))
						.collect(Collectors.toList());
		return bookDtos;
	}

	@Override
	public List<DisplayBookDto> findByCategory(Long categoryId, String sortBy) {
		Direction direction= Direction.ASC;
		if (sortBy.equals("price-down")) {
			sortBy = "price";
			direction = Direction.DESC;
		} else if(sortBy.equals("price-up")) {
			sortBy = "price";
		}
		List<Book> books=bookRepo.findAllByCategory(categoryId, Sort.by(direction,sortBy));
		List<DisplayBookDto> bookDtos = new ArrayList<>();
		bookDtos = books.stream().map(x->mapperBook.fromBookToDisplayBookDto(x))
						.collect(Collectors.toList());
		return bookDtos;
	}
};












