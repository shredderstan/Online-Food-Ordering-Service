package com.example.food.cart.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartResponse {

	private Long id;
	private Long restaurantId;
	private BigDecimal totalAmount;
	private List<CartItemResponse> items;
	
}
