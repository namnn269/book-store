package com.nam.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nam.entity.Book;

@Repository
@Transactional
public interface IBookRepository extends JpaRepository<Book, Long> {

	@Query("SELECT DISTINCT b FROM Book b "
			+ " JOIN b.authors a "
			+ " JOIN b.category c "
			+ " WHERE b.bookTitle LIKE %?1% OR a.fullname LIKE %?1% OR c.categoryTitle LIKE %?1%")
	Page<Book> findAll(String key, Pageable pageable);
	
	@Query("SELECT DISTINCT b FROM Book b "
			+ " JOIN b.authors a "
			+ " JOIN b.category c "
			+ " WHERE b.bookTitle LIKE %?1% OR a.fullname LIKE %?1% OR c.categoryTitle LIKE %?1%")
	List<Book> findAllBySortAndSearch(String key, Sort sort);

	@Query("SELECT DISTINCT b FROM Book b "
			+ " JOIN b.category c "
			+ " WHERE c.id = ?1")
	List<Book> findAllByCategory(Long categoryId, Sort by);
	

}
