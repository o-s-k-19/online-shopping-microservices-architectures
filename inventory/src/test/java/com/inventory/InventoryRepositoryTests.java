package com.inventory;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.inventory.domain.InventoryRepository;

// @DataJpaTest(
// properties = {
// 	"spring.test.database.replace=NONE",
// 	"spring.flyway.enabled=false",
//     "spring.jpa.hibernate.ddl-auto=create",
//     "spring.jpa.defer-datasource-initialization=true",
//     "spring.datasource.url=jdbc:postgresql://localhost:5432/test",
//     "spring.datasource.username=root",
//     "spring.datasource.password=root"
//   }
// )
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/test-data.sql")
class InventoryRepositoryTests {

	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14-alpine");

	@BeforeAll
	static void beforeAll() {
		postgres.start();
	}

	@AfterAll
	static void afterAll() {
		postgres.stop();
	}

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.flyway.enabled", () -> false);
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
		registry.add("spring.jpa.defer-datasource-initialization", () -> true);
		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.username", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
	}

	@Autowired
	private InventoryRepository inventoryRepository;

	@Test
	void shouldReturnAllProductInInventory() {
		Assertions.assertThat(inventoryRepository.findAll()).hasSize(4);
	}

	@Test
	void shouldFindProductInInventoryBySkuCode() {
		Assertions.assertThat(inventoryRepository.findBySkuCode("Iphone_16")).isPresent();
	}

}
