package com.nam.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nam.entity.Book;

@Repository
@Transactional
public interface IBookRepository extends JpaRepository<Book, Long> {

	
//	@Query("SELECT DISTINCT b FROM Book b "
//			+ " LEFT JOIN b.authors a "
//			+ " LEFT JOIN b.category c "
//			+ " WHERE b.bookTitle LIKE %?1% OR a.fullname LIKE %?1% OR c.categoryTitle LIKE %?1%")
//	Page<Book> findAll(String key, Pageable pageable);
//	
//	@Query("SELECT DISTINCT b FROM Book b "
//			+ " LEFT JOIN b.authors a "
//			+ " LEFT JOIN b.category c "
//			+ " WHERE b.bookTitle LIKE %?1% OR a.fullname LIKE %?1% OR c.categoryTitle LIKE %?1%")
//	List<Book> findAllBySortAndSearch(String key, Sort sort);
//
//	@Query("SELECT DISTINCT b FROM Book b "
//			+ " LEFT JOIN b.category c "
//			+ " WHERE c.id = ?1")
//	List<Book> findAllByCategory(Long categoryId, Sort by);

	// tìm kiếm page sách theo search key, category ID 
	@Query(value = "SELECT DISTINCT b FROM Book b "
			+ " LEFT JOIN b.category c "
			+ " LEFT JOIN b.authors a "
			+ " WHERE c.id = ?2 "
			+ " 	AND( b.bookTitle LIKE %?1% OR b.publishingYear LIKE %?1% OR a.fullname LIKE %?1% OR c.categoryTitle LIKE %?1% ) ")
	Page<Book> findAllBySearchAndFilterAndSort(String searchKey, long categoryId, Pageable pageable);
	
	// tìm kiếm page sách theo search key
	@Query(value = "SELECT DISTINCT b FROM Book b "
			+ " LEFT JOIN b.category c "
			+ " LEFT JOIN b.authors a "
			+ " WHERE b.bookTitle LIKE %?1% OR b.publishingYear LIKE %?1% OR a.fullname LIKE %?1% OR c.categoryTitle LIKE %?1% ")
	Page<Book> findAllBySearchAndSort(String searchKey, Pageable pageable);
}












