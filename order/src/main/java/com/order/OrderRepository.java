package com.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
