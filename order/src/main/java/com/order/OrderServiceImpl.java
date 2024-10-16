/* (C)2024 */
package com.order;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    private final ApplicationPropertiesConfig applicationPropertiesConfig;

    @Override
    public OrderResponse create(OrderRequest orderRequest) throws OutOfStockException {

        List<InventoryRequest> inventoryRequests = orderRequest.getOrderLineItems().stream()
                .map(oli -> new InventoryRequest(oli.getSkuCode(), oli.getQuantity())).collect(Collectors.toList());

        Span inventoryServiceLookup = tracer.nextSpan().name("inventoryServiceLookUP");

        try (Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceLookup.start())) {

            if (this.inStock(inventoryRequests)) {
                Order savedOrder = this.saveOrder(orderRequest);
                this.publishNewOrder(savedOrder);
                return orderMapper.mapToOrderResponse(savedOrder);
            } else {
                throw new OutOfStockException("Order cannot be placed because out of stock");
            }

        } finally {
            inventoryServiceLookup.end();
        }
    }

    private Order saveOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setNumber(UUID.randomUUID().toString());
        order.setLineItems(orderRequest.getOrderLineItems().stream().map(orderMapper::mapToOrderLineItem)
                .collect(Collectors.toList()));
        Order savedOrder = orderRepository.save(order);
        log.info("Order : {} has been saved", savedOrder);
        return savedOrder;
    }

    private boolean inStock(List<InventoryRequest> inventoryRequestList) {
        InventoryResponse[] inventoryResponses = webClientBuilder.build().post().uri("http://inventory/api/inventories")
                .contentType(MediaType.APPLICATION_JSON).bodyValue(inventoryRequestList).retrieve()
                .bodyToMono(InventoryResponse[].class).block();

        return Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);
    }

    private void publishNewOrder(Order order) {
        OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
        BeanUtils.copyProperties(order, orderPlacedEvent);
        sendMessage(orderPlacedEvent);
    }

    @Override
    public Page<OrderResponse> findAll(int size, int page) {
        return orderRepository.findAll(PageRequest.of(page, size)).map(orderMapper::mapToOrderResponse);
    }

    private void sendMessage(OrderPlacedEvent orderPlacedEvent) {
        CompletableFuture<SendResult<String, OrderPlacedEvent>> future = kafkaTemplate
                .send(applicationPropertiesConfig.getDefaultTopic(), orderPlacedEvent);
        future.toCompletableFuture().whenComplete((result, exception) -> {
            if (exception == null) {
                log.info("\n\nmessage : {}\n offset: {}", orderPlacedEvent, result.getRecordMetadata().offset());
            } else {
                log.info("exception : {}", exception.getMessage());
            }
        });
    }

    @Override
    public OrderResponse findById(Long id) throws OrderNotFoundException {
        return orderRepository.findById(id).map(orderMapper::mapToOrderResponse)
                .orElseThrow(() -> new OrderNotFoundException(String.format("Order not found for id %s", id)));
    }

    @Override
    public void deleteById(Long id) throws OrderNotFoundException {
        orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(String.format("Order not found for id %s", id)));
        orderRepository.deleteById(id);
    }
}
