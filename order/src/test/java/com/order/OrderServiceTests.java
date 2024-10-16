package com.order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import io.micrometer.tracing.Tracer;

@ExtendWith(MockitoExtension.class)
class OrderServiceTests {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private Tracer tracer;
    @Mock
    private KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    @Mock
    private ApplicationPropertiesConfig applicationPropertiesConfig;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void shouldFindOrderById() throws OrderNotFoundException {
        Optional<Order> optionalOrder = Optional.of(new Order(1l, "123", List.of()));

        when(orderRepository.findById(anyLong())).thenReturn(optionalOrder);
        when(orderMapper.mapToOrderResponse(any(Order.class))).thenReturn(new OrderResponse(1l, "123", List.of()));

        OrderResponse orderResponse = orderService.findById(1l);

        assertThat(orderResponse.getId()).isEqualTo(optionalOrder.get().getId());
    }

    @Test
    void shouldCreateOrder() {

    }

    @Test
    void shouldFindAllOrders() {

    }

    @Test
    void shouldDeleteOrderByid() {

    }
}
