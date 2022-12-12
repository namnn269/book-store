package com.nam.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.nam.dto.DisplayBookDto;
import com.nam.dto.NewBookDto;
import com.nam.entity.Book;
import com.nam.exception_mesage.Message;

public interface IBookService {
	Optional<Book> findById(Long id);

	List<Book> findAll();
	
	Message save(NewBookDto bookDto);

	List<DisplayBookDto> findAll(int pageNo, int pageSize, String sortBy);

	Page<Book> getPageBook(int pageNo, int pageSize, String sortBy);

	Message delete(Long id);

	NewBookDto getUpdateBook(Long id);

	DisplayBookDto findBookDetailById(Long id);

	List<DisplayBookDto> findAllBySearchKey(String sortBy, String searchKey);

	List<DisplayBookDto> findByCategory(Long categoryId, String sortBy);
}
