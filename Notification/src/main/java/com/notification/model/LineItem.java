package com.notification.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class LineItem {

	private Long id;
	private String skuCode;
	private BigDecimal price;
	private int quantity;
	
}
