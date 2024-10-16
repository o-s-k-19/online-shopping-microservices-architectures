package com.order;

import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

	public LineItem mapToOrderLineItem(OrderLineItemDTO orderLineItemDTO) {
		LineItem orderLineItem = new LineItem();
		BeanUtils.copyProperties(orderLineItemDTO, orderLineItem);
		return orderLineItem;
	}
	
	public OrderLineItemDTO mapToOrderLineItemDTO(LineItem orderLineItem) {
		OrderLineItemDTO orderLineItemDTO = new OrderLineItemDTO();
		BeanUtils.copyProperties(orderLineItem, orderLineItemDTO);
		return orderLineItemDTO;
	}
	
	public OrderResponse mapToOrderResponse(Order order) {
		OrderResponse orderResponse = new OrderResponse();
		BeanUtils.copyProperties(order, orderResponse);
		orderResponse.setOrderLineItems(
				order.getLineItems()
				.stream()
				.map(this::mapToOrderLineItemDTO)
				.collect(Collectors.toList())
				);
		return orderResponse;
	}
}
