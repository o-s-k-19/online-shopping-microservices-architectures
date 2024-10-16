package com.inventory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.inventory.application.InventoryRequest;
import com.inventory.application.InventoryResponse;
import com.inventory.application.InventoryServiceImpl;
import com.inventory.domain.Inventory;
import com.inventory.domain.InventoryRepository;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTests {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @Test
    void shouldReturnListOfProductInStockFromRequest() {
        List<InventoryRequest> inventoryRequestList = List.of(new InventoryRequest("sku1", 2),
                new InventoryRequest("sku2", 11));
        List<Inventory> inventoryList = List.of(new Inventory(1l, "sku1", 5), new Inventory(2l, "sku2", 5));

        when(inventoryRepository.findByskuCodeIn(anyList())).thenReturn(inventoryList);

        List<InventoryResponse> result = inventoryService.isInStock(inventoryRequestList);

        assertThat(result.size()).isEqualTo(inventoryRequestList.size());
    }

    @Test
    void shouldReturnListOfProductInStockFromRequestBySkuCodeOnly() {
        List<String> skuList = List.of("sku1", "sku2");
        List<Inventory> inventoryList = List.of(new Inventory(1l, "sku1", 5), new Inventory(2l, "sku2", 5));

        when(inventoryRepository.findByskuCodeIn(anyList())).thenReturn(inventoryList);

        List<InventoryResponse> result = inventoryService.isInStockBySkuCodeOnly(skuList);

        assertThat(result.size()).isEqualTo(skuList.size());
    }
}
