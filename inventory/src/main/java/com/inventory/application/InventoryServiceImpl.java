package com.inventory.application;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.domain.Inventory;
import com.inventory.domain.InventoryRepository;
import com.inventory.domain.OrderPlacedEvent;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@KafkaListener(topics = "placed_orders")
@Slf4j
public class InventoryServiceImpl implements InventoryService {

	private final InventoryRepository inventoryRepository;

	@Override
	public List<InventoryResponse> isInStock(List<InventoryRequest> inventoryRequests) {
		List<String> skuCodes = inventoryRequests.stream().map(ir -> ir.getSkuCode()).toList();

		HashMap<String, InventoryRequest> inventoryDict = new HashMap<>();
		inventoryRequests.forEach(ir -> inventoryDict.put(ir.getSkuCode(), ir));

		return inventoryRepository.findByskuCodeIn(skuCodes).stream()
				.map(s -> InventoryResponse.builder().skuCode(s.getSkuCode()).availableQuantity(s.getQuantity())
						.inStock(s.getQuantity() >= inventoryDict.get(s.getSkuCode()).getQuantity()).build())
				.toList();
	}

	@Override
	public List<InventoryResponse> isInStockBySkuCodeOnly(List<String> skuCode) {
		return inventoryRepository.findByskuCodeIn(skuCode).stream().map(i -> InventoryResponse.builder()
				.skuCode(i.getSkuCode()).availableQuantity(i.getQuantity()).inStock(i.getQuantity() > 0).build())
				.toList();
	}

	@Override
	@KafkaHandler
	public void updateInventory(OrderPlacedEvent orderPlacedEvent) {
		log.info("\n\n{}\n\n", orderPlacedEvent);
		orderPlacedEvent.getLineItems().stream().forEach(item -> {
			Optional<Inventory> optInventory = inventoryRepository.findBySkuCode(item.getSkuCode());
			if (optInventory.isPresent()) {
				Inventory inventory = optInventory.get();
				inventory.setQuantity(
						inventory.getQuantity() > item.getQuantity() ? inventory.getQuantity() - item.getQuantity()
								: item.getQuantity() - inventory.getQuantity());
				inventoryRepository.save(inventory);
			}
		});

	}

}
