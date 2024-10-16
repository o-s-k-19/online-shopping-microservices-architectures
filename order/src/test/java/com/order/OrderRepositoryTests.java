package com.order;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// @Testcontainers
@TestPropertySource(properties = { "spring.test.database.replace=none",
                "spring.datasource.url=jdbc:tc:mysql:8.0:///db" })
public class OrderRepositoryTests {

}
