package com.inventory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import com.inventory.model.Inventory;
import com.inventory.repository.InventoryRepository;

@SpringBootApplication
@EnableEurekaClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}
	
	@Bean
	public CommandLineRunner start(InventoryRepository inventoryRepository) {
		return (args ->{
			inventoryRepository.save(new Inventory(null, "iphone_14", 5));
			inventoryRepository.save(new Inventory(null, "samsung_s22", 6));
		});
	}

}
