package com.example.food.order.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {

	private Long id;
	private Long menuItemId;
	private String itemName;
	private Integer quantity;
	private BigDecimal unitPrice;
	private BigDecimal subTotal;
}

