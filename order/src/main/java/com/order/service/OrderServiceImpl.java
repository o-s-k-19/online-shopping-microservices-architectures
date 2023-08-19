package com.order.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.order.dto.InventoryRequest;
import com.order.dto.InventoryResponse;
import com.order.dto.OrderRequest;
import com.order.dto.OrderResponse;
import com.order.mapper.OrderMapper;
import com.order.model.Order;
import com.order.model.OrderLineItem;
import com.order.repository.OrderRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final OrderMapper orderMapper;
	private final WebClient.Builder webClientBuilder;
	
	@Override
	public boolean placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		order.setOrderLineItems(orderRequest.getOrderLineItems().stream()
				.map(orderMapper::mapToOrderLineItem)
				.collect(Collectors.toList()));
		
		List<String> skuCodes = order.getOrderLineItems().stream()
				.map(OrderLineItem::getSkuCode)
				.collect(Collectors.toList());
				
//		List<InventoryRequest> inventoryRequests = order.getOrderLineItems().stream()
//				.map(oli -> new InventoryRequest(oli.getSkuCode(),oli.getQuantity()))
//				.collect(Collectors.toList());
		
		 InventoryResponse[] inventoryResponse = webClientBuilder.build().get()
				.uri("http://inventory/api/inventories",uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
				.retrieve()
				.bodyToMono(InventoryResponse[].class)
				.block();
		
//		 InventoryResponse[] inventoryResponses = webClientBuilder.build().post()
//				 .uri("http://localhost:8082/api/inventories")
//				 .attribute("inventoryRequest", inventoryRequests)
//				 .retrieve()
//				 .bodyToMono(InventoryResponse[].class)
//				 .block();
				 
				 
				 
		 boolean inStock = Arrays.stream(inventoryResponse).allMatch(InventoryResponse::isInStock);
		
		 if(inStock) {
			Order placedOrder = orderRepository.save(order);
			log.info("Order : {} has been placed",placedOrder);
			return true;
		}else {
			throw new IllegalArgumentException("Products are not in stock");
		}
	}

	@Override
	public List<OrderResponse> getAllOrders() {
		return orderRepository.findAll().stream()
				.map(orderMapper::mapToOrderResponse)
				.collect(Collectors.toList());
	}

}
