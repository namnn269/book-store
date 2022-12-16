package com.nam.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.nam.entity.Author;
import com.nam.exception_mesage.Message;

public interface IAuthorService {

	Optional<Author> findById(Long id);

	List<Author> findAll(int pageNo, int pageSize, String sortBy, String searchFor);

	Page<Author> getPageAuthor(int pageNo, int pageSize, String sortBy, String searchFor);

	Message save(Author author);

	Message delete(Long id);

	List<Author> findAll();

}
