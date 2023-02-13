package com.nam.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.nam.entity.Order;
import com.nam.entity.User;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {

	Page<Order> findByUser(User user, Pageable pageable);

	@Query(value = "SELECT * FROM orders o WHERE o.user_id=?1 AND (o.order_status=?2 OR o.order_status=?3) "
			+ " ORDER BY o.order_date DESC", nativeQuery = true)
	List<Order> findByUserAndStatus(User user, int status, int status2);

	Page<Order> findByStatus(int status, Pageable pageable);

	Page<Order> findByStatusAndCancelBy(int status,int cancelBy, Pageable pageable);

	Page<Order> findByStatusAndCancelByAndUser(int status,int cancelBy, User user ,Pageable pageable);

	Page<Order> findByStatusAndUser(int i, User user, Pageable pageable);

	@Query(value = "SELECT DISTINCT o FROM Order o WHERE o.status <> 0 AND o.user = :user")
	Page<Order> findAllByUser(@RequestParam User user, Pageable pageable);

}
