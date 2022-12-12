package com.nam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nam.entity.Category;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long>{
	Category findByCategoryTitle(String categoryTitle);

}
