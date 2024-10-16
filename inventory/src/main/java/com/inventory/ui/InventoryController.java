package com.inventory.ui;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.application.InventoryRequest;
import com.inventory.application.InventoryResponse;
import com.inventory.application.InventoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {

	private final InventoryService inventoryService;
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<InventoryResponse> checkIfInStock(@RequestBody @Valid InventoryRequest[] inventoryRequests) {
		return inventoryService.isInStock(Arrays.asList(inventoryRequests));
	}
	
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<InventoryResponse> ckeckInStockBySkuCodeOnly(@RequestParam List<String> skuCode){
		return inventoryService.isInStockBySkuCodeOnly(skuCode);
	}
}
