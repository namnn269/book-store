package com.nam.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nam.entity.Author;

@Repository
public interface IAuthorRepository extends JpaRepository<Author, Long> {
	
	Author findByFullname(String fullname);

	@Query(value = "SELECT a FROM Author a WHERE a.fullname LIKE %?1%")
	Page<Author> findAllBySearchAndPageble(String seachFor, Pageable pageable);
}
