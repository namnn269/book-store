package com.nam.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.nam.dto.AdminBookDto;
import com.nam.dto.DisplayBookDto;
import com.nam.entity.Book;
import com.nam.exception_mesage.Message;

public interface IBookService {
	Optional<Book> findById(Long id);

	List<Book> findAll();

	Message save(AdminBookDto bookDto, MultipartFile multiFile) throws IOException;

	List<DisplayBookDto> findAll(int pageNo, int pageSize, String searchKey, long categoryId, String sortByPrice);

	Page<Book> getPageBook(int pageNo, int pageSize, String searchKey, long categoryId, String sortByPrice);

	Message delete(Long id);

	AdminBookDto getUpdateBook(Long id);

	DisplayBookDto findBookDetailById(Long id);

	List<DisplayBookDto> findBestSellerBooks();


}
