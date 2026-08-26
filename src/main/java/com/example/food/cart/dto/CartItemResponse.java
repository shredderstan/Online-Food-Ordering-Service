package com.example.food.cart.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class CartItemResponse {
	
	private Long id;
	private Long menuItemId;
	private String menuItemName;
	private BigDecimal unitPrice;
	private Integer quantity;
	private BigDecimal subTotal;

}
