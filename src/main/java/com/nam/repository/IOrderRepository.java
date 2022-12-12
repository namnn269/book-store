package com.nam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nam.entity.Order;
import com.nam.entity.User;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long>{
	
	List<Order> findByUser(User user);
	
	@Query(value = "SELECT * FROM orders o WHERE o.user_id=?1 AND o.order_status=?2 ", nativeQuery = true)
	List<Order> findByUserAndStatus(User user, int status);
	
	@Query(value = "SELECT * FROM orders o WHERE o.order_status=?1 ", nativeQuery = true)
	List<Order> findByStatus(int status);
}
