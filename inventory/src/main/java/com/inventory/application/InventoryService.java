package com.inventory.application;

import java.util.List;

import com.inventory.domain.OrderPlacedEvent;

public interface InventoryService {

	List<InventoryResponse> isInStock(List<InventoryRequest> inventoryRequests);

	List<InventoryResponse> isInStockBySkuCodeOnly(List<String> skuCode);

	void updateInventory(OrderPlacedEvent orderPlacedEvent);

}
