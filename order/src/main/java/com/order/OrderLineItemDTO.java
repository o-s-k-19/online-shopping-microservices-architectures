package com.order;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderLineItemDTO {

	private Long id;
	private String skuCode;
	private BigDecimal price;
	private int quantity;
}
