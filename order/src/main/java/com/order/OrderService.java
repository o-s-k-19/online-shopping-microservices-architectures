package com.order;

import org.springframework.data.domain.Page;

public interface OrderService {

	OrderResponse findById(Long id) throws OrderNotFoundException;

	void deleteById(Long id) throws OrderNotFoundException;

	OrderResponse create(OrderRequest orderRequest) throws OutOfStockException;

	Page<OrderResponse> findAll(int size, int page);

}
