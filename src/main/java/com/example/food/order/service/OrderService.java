package com.example.food.order.service;

import java.util.List;

import com.example.food.order.dto.CheckoutRequest;
import com.example.food.order.dto.OrderResponse;
import com.example.food.order.dto.UpdateOrderStatusRequest;

public interface OrderService {

	OrderResponse checkout(String customerEmail, CheckoutRequest request);

	List<OrderResponse> getMyOrders(String customerEmail);

	OrderResponse getOrderById(Long orderId);

	List<OrderResponse> getOrdersByRestaurant(Long restaurantId);

	OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request);

	OrderResponse cancelOrder(Long orderId, String customerEmail);
}

