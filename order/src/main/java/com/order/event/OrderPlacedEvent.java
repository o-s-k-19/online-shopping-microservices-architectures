package com.order.event;

import java.util.List;

import com.order.model.LineItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlacedEvent {
	private String number;
	private List<LineItem> lineItems;
}
