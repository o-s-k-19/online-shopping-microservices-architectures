package com.inventory.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.dto.InventoryRequest;
import com.inventory.dto.InventoryResponse;
import com.inventory.model.Inventory;
import com.inventory.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

	private final InventoryRepository inventoryRepository;
	@Override
	public boolean isInStock(String skuCode, int quantity) {
		Optional<Inventory> optInventory = inventoryRepository.findBySkuCode(skuCode);
		return optInventory.isPresent() && optInventory.get().getQuantity() >= quantity;
	}
	@Override
	public List<InventoryResponse> isInStock(List<InventoryRequest> inventoryRequests) {
		List<String> skuCodes = inventoryRequests.stream()
				.map(ir -> ir.getSkuCode())
				.collect(Collectors.toList());
		
		HashMap<String,InventoryRequest> inventoryDict = new HashMap<>();
		inventoryRequests.forEach(ir -> inventoryDict.put(ir.getSkuCode(), ir));
		
		return inventoryRepository.findByskuCodeIn(skuCodes).stream()
				.map(s-> InventoryResponse.builder()
						.skuCode(s.getSkuCode())
						.availableQuantity(s.getQuantity())
						.inStock(s.getQuantity() > inventoryDict.get(s.getSkuCode()).getQuantity())
						.build())				
						.collect(Collectors.toList());
	} 
	@Override
	public List<InventoryResponse> isInStockBySkuCodeOnly(List<String> skuCode) {
		return inventoryRepository.findByskuCodeIn(skuCode).stream()
				.map(i -> InventoryResponse.builder()
						.skuCode(i.getSkuCode())
						.availableQuantity(i.getQuantity())
						.inStock(i.getQuantity()> 0)
						.build())
						.collect(Collectors.toList());
	}
	
	

}
