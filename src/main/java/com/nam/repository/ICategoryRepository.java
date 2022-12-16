package com.nam.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nam.entity.Category;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long>{
	Category findByCategoryTitle(String categoryTitle);

	@Query("SELECT DISTINCT c FROM Category c WHERE c.categoryTitle LIKE %?1%")
	Page<Category> findAllByPageableAndSearch( String searchFor, Pageable pageable) ;
	
	
}
