package com.nam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nam.entity.OrderDetail;
@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetail, Long> {

}
