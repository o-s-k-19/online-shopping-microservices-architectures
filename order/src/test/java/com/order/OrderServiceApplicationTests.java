package com.order;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = { "spring.test.database.replace=none",
		"spring.datasource.url=jdbc:tc:mysql:8.0:///db" })
class OrderServiceApplicationTests {

	@LocalServerPort
	private Integer port;

	@Container
	static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0");

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username", mySQLContainer::getUsername);
		registry.add("spring.datasource.password", mySQLContainer::getPassword);
	}

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private MockMvc mockMvc;

	@BeforeAll
	static void beforeAll() {
		mySQLContainer.start();
	}

	@AfterAll
	static void afterAll() {
		mySQLContainer.stop();
	}

	@BeforeEach
	void reset() {
		orderRepository.deleteAll();
	}

	@Test
	void shouldFindAllOrder() throws Exception {
		List<Order> orderList = List.of(
				new Order(1l, "N1111", List.of(new LineItem(1l, "sku1", BigDecimal.valueOf(5), 2))),
				new Order(2l, "N2222", List.of(new LineItem(2l, "sku2", BigDecimal.valueOf(2), 3))),
				new Order(3l, "N3333", List.of(new LineItem(3l, "sku3", BigDecimal.valueOf(3), 4))));
		orderRepository.saveAll(orderList);

		mockMvc.perform(get("/api/orders")).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(orderList.size()))).andDo(print());
	}

	@Test
	void shouldFindOrderById() throws Exception {
		List<Order> orderList = List
				.of(new Order(1l, "N1111", List.of(new LineItem(1l, "sku1", BigDecimal.valueOf(5), 2))));
		orderRepository.saveAll(orderList);

		mockMvc.perform(get("/api/orders/1")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id", is(1)))
				.andDo(print());
	}

	@Test
	void shouldCreateOrder() throws JsonProcessingException, Exception {
		OrderRequest orderRequest = new OrderRequest(
				List.of(new OrderLineItemDTO(0l, "sku5", BigDecimal.valueOf(7), 3)));

		mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(orderRequest))).andExpect(status().isCreated()).andDo(print());
	}

	@Test
	void shouldDeleteOrderById() throws Exception {
		List<Order> orderList = List
				.of(new Order(1l, "N1111", List.of(new LineItem(1l, "sku1", BigDecimal.valueOf(5), 2))));
		orderRepository.saveAll(orderList);

		mockMvc.perform(delete("/api/orders/1")).andExpect(status().isOk()).andDo(print());
	}

}
