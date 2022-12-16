package com.nam.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.nam.entity.Category;
import com.nam.exception_mesage.Message;

public interface ICategoryService {
	Optional<Category> findById(Long id);

	List<Category> findAll();

	Message save(Category category);
	
	Message delete(Long id);

	Page<Category> getPageCategory(int pageNo, int pageSize, String sortBy, String searchFor);

	List<Category> findAll(int pageNo, int pageSize, String sortBy, String seachFor);
}
