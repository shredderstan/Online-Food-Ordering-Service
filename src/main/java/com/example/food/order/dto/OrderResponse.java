package com.example.food.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.food.order.entity.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

	private Long id;
	private Long userId;
	private String customerName;
	private String customerEmail;
	private Long restaurantId;
	private String restaurantName;
	private OrderStatus status;
	private String deliveryAddress;
	private String deliveryNotes;
	private BigDecimal totalAmount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private List<OrderItemResponse> items;
}

