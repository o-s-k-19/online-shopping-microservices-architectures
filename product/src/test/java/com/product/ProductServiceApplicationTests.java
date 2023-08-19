package com.product;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.dto.ProductRequest;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static
	MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
	
	@Autowired
	private MockMvc mockMvc; 
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}
	
	@Test
	void shouldCreateProduct() throws JsonProcessingException, Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(getProductRequest()))
				).andExpect(status().isCreated());
	}
	
	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("Iphone 14")
				.description("apple smartphone")
				.price(BigDecimal.valueOf(1199))
				.build();			
	}

}
