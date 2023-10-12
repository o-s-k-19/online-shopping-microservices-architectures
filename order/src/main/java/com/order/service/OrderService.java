package com.order.service;

import java.util.List;

import com.order.dto.OrderRequest;
import com.order.dto.OrderResponse;

public interface OrderService {

	String placeOrder(OrderRequest orderRequest);

	List<OrderResponse> getAllOrders();
}
