package com.nam.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.nam.entity.Category;
import com.nam.exception_mesage.Message;

public interface ICategoryService {
	Optional<Category> findById(Long id);

	List<Category> findAll();

	List<Category> findAll(int pageNo, int pageSize, String sortBy);

	Page<Category> getPageCategory(int pageNo, int pageSize, String sortBy);

	Message save(Category category);
	
	Message delete(Long id);
}
