package com.order.mapper;

import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.order.dto.OrderLineItemDTO;
import com.order.dto.OrderResponse;
import com.order.model.Order;
import com.order.model.OrderLineItem;

@Component
public class OrderMapper {

	public OrderLineItem mapToOrderLineItem(OrderLineItemDTO orderLineItemDTO) {
		OrderLineItem orderLineItem = new OrderLineItem();
		BeanUtils.copyProperties(orderLineItemDTO, orderLineItem);
		return orderLineItem;
	}
	
	public OrderLineItemDTO mapToOrderLineItemDTO(OrderLineItem orderLineItem) {
		OrderLineItemDTO orderLineItemDTO = new OrderLineItemDTO();
		BeanUtils.copyProperties(orderLineItem, orderLineItemDTO);
		return orderLineItemDTO;
	}
	
	public OrderResponse mapToOrderResponse(Order order) {
		OrderResponse orderResponse = new OrderResponse();
		BeanUtils.copyProperties(order, orderResponse);
		orderResponse.setOrderLineItems(
				order.getOrderLineItems()
				.stream()
				.map(this::mapToOrderLineItemDTO)
				.collect(Collectors.toList())
				);
		return orderResponse;
	}
}
