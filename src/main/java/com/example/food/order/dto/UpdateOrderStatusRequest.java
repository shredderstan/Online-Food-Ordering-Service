package com.example.food.order.dto;

import com.example.food.order.entity.OrderStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusRequest {

	@NotNull(message = "Order status is required")
	private OrderStatus status;
}

