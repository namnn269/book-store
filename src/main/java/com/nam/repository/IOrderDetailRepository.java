package com.nam.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nam.entity.Book;
import com.nam.entity.OrderDetail;
@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Long> {

	// tìm kiếm top ID sách bán chạy nhất
//		@Query(value = " SELECT distinct od.book_id "
//				+ " FROM book_store.order_details od "
//				+ " JOIN orders o ON o.id = od.order_id "
//				+ " WHERE o.order_status = 2 "
//				+ " group by book_id "
//				+ " order by sum(od.amount) desc "
//				+ " limit ?1 ", nativeQuery = true)
//		List<Long> findTopBestSellerID(int limit);
		
	// tìm kiếm top sách bán chạy nhất
		@Query(value = " SELECT od.book "
				+ " FROM OrderDetail od "
				+ " JOIN od.order o " 
				+ " WHERE o.status = 2 "
				+ " GROUP BY od.book "
				+ " ORDER BY sum(od.amount) desc ")
		Page<Book> findTopBook(Pageable pageable);
}
