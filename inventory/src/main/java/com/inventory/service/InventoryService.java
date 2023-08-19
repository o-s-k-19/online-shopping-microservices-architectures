package com.inventory.service;

import java.util.List;

import com.inventory.dto.InventoryRequest;
import com.inventory.dto.InventoryResponse;

public interface InventoryService {

	boolean isInStock(String skuCode,int quantity);

	List<InventoryResponse> isInStock(List<InventoryRequest> inventoryRequests);

	List<InventoryResponse> isInStockBySkuCodeOnly(List<String> skuCode);

}
