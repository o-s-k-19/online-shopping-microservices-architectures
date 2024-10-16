/* (C)2024 */
package com.product;

import com.product.model.Product;
import com.product.repository.ProductRepository;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner start(ProductRepository productRepository) {
        return (args -> {
            productRepository.save(
                    Product.builder()
                            .name("iphone_14")
                            .description("Apple smartphone")
                            .price(BigDecimal.valueOf(1500))
                            .build());
            productRepository.save(
                    Product.builder()
                            .name("samsung_s22")
                            .description("Samsung smartphone")
                            .price(BigDecimal.valueOf(1200))
                            .build());
            productRepository.save(
                    Product.builder()
                            .name("MacBook")
                            .description("Apple computer")
                            .price(BigDecimal.valueOf(2500))
                            .build());
        });
    }
}
