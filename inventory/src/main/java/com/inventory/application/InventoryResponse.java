package com.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponse {
	
	private String skuCode;
	private int availableQuantity;
	private boolean inStock;
}
