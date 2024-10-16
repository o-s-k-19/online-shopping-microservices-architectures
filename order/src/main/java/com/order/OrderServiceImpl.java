package com.order.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.reactive.function.client.WebClient;

import com.order.config.ApplicationPropertiesConfig;
import com.order.dto.InventoryRequest;
import com.order.dto.InventoryResponse;
import com.order.dto.OrderRequest;
import com.order.dto.OrderResponse;
import com.order.event.OrderPlacedEvent;
import com.order.mapper.OrderMapper;
import com.order.model.Order;
import com.order.model.LineItem;
import com.order.repository.OrderRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final OrderMapper orderMapper;
	private final WebClient.Builder webClientBuilder;
	private final Tracer tracer;
	private final KafkaTemplate<String,OrderPlacedEvent> kafkaTemplate;
	
	private final ApplicationPropertiesConfig applicationPropertiesConfig;
	
	@Override
	public String placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setNumber(UUID.randomUUID().toString());
		order.setLineItems(orderRequest.getOrderLineItems().stream()
				.map(orderMapper::mapToOrderLineItem)
				.collect(Collectors.toList()));
		
		List<String> skuCodes = order.getLineItems().stream()
				.map(LineItem::getSkuCode)
				.collect(Collectors.toList());
				
		List<InventoryRequest> inventoryRequests = order.getLineItems().stream()
				.map(oli -> new InventoryRequest(oli.getSkuCode(),oli.getQuantity()))
				.collect(Collectors.toList());
		

		
		// custom span for tracing
		Span inventoryServiceLookup = tracer.nextSpan().name("inventoryServiceLookUP");
		
		try(Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceLookup.start())){
		    
		    InventoryResponse[] inventoryResponses = webClientBuilder.build().post()
				 .uri("http://inventory/api/inventories")
				 .contentType(MediaType.APPLICATION_JSON)
				 .bodyValue(inventoryRequests)
				 .retrieve()
				 .bodyToMono(InventoryResponse[].class)
				 .block();
		    
//			 InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
//			.uri("http://localhost:8082/api/inventories",uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
//			.retrieve()
//			.bodyToMono(InventoryResponse[].class)
//			.block();
		 
		 boolean inStock = false;
		 
		 if(inventoryResponses.length > 0) {
		     inStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);
		 }
		 
		 if(inStock) {
			Order placedOrder = orderRepository.save(order);
			log.info("Order : {} has been placed",placedOrder);
			
			OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
			BeanUtils.copyProperties(order, orderPlacedEvent);
			
			sendMessage(orderPlacedEvent);
			
			return "Order is placed successfully !";
		}else {
			return "Order can not be placed at this moment, because product are out of stock";
		}
		    
		}finally {
		    inventoryServiceLookup.end();
		}
		
		 
	}

	@Override
	public List<OrderResponse> getAllOrders() {
		return orderRepository.findAll().stream()
				.map(orderMapper::mapToOrderResponse)
				.collect(Collectors.toList());
	}
	
	private void sendMessage(OrderPlacedEvent orderPlacedEvent) {
		 ListenableFuture<SendResult<String, OrderPlacedEvent>> future = kafkaTemplate.send(applicationPropertiesConfig.getDefaultTopic(),orderPlacedEvent);
		future.completable().whenComplete((result,exception)->{
			if(exception == null) {
				log.info("\n\nmessage : {}\n offset: {}", orderPlacedEvent,result.getRecordMetadata().offset());
			}
			else {
				log.info("exception : {}",exception.getMessage());
			}
		});
	}

}
