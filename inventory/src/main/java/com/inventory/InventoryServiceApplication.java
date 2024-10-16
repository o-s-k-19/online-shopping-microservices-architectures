/* (C)2024 */
package com.inventory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.inventory.domain.Inventory;
import com.inventory.domain.InventoryRepository;

@SpringBootApplication
// @EnableEurekaClient
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner start(InventoryRepository inventoryRepository) {
        return (args -> {
        	inventoryRepository.deleteAll();
            inventoryRepository.save(new Inventory(null, "Iphone_16", 3));
            inventoryRepository.save(new Inventory(null, "samsung_s22", 6));
            inventoryRepository.save(new Inventory(null, "MacBook", 2));
        });
    }
}
