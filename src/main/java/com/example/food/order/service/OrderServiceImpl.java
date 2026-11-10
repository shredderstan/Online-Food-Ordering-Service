package com.example.food.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.food.cart.entity.Cart;
import com.example.food.cart.repository.CartRepository;
import com.example.food.exception.ResourceNotFoundException;
import com.example.food.order.dto.CheckoutRequest;
import com.example.food.order.dto.OrderItemResponse;
import com.example.food.order.dto.OrderResponse;
import com.example.food.order.dto.UpdateOrderStatusRequest;
import com.example.food.order.entity.Order;
import com.example.food.order.entity.OrderItem;
import com.example.food.order.entity.OrderStatus;
import com.example.food.order.repository.OrderRepository;
import com.example.food.restaurant.entity.Restaurant;
import com.example.food.restaurant.repository.RestaurantRepository;
import com.example.food.user.entity.User;
import com.example.food.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final CartRepository cartRepository;
	private final UserRepository userRepository;
	private final RestaurantRepository restaurantRepository;

	@Override
	public OrderResponse checkout(String customerEmail, CheckoutRequest request) {
		User user = userRepository.findByEmail(customerEmail)
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + customerEmail));

		Cart cart = cartRepository.findByUserId(user.getId())
				.orElseThrow(() -> new IllegalArgumentException("Cart is empty or does not exist"));

		if (cart.getItems() == null || cart.getItems().isEmpty()) {
			throw new IllegalArgumentException("Cannot checkout with an empty cart");
		}

		Restaurant restaurant = restaurantRepository.findById(cart.getRestaurantId())
				.orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + cart.getRestaurantId()));

		if (!restaurant.isActive()) {
			throw new IllegalArgumentException("Restaurant is currently not accepting orders");
		}

		// Verify availability of items
		for (var cartItem : cart.getItems()) {
			if (Boolean.FALSE.equals(cartItem.getMenuItem().getAvailable())) {
				throw new IllegalArgumentException("Item '" + cartItem.getMenuItem().getName() + "' is no longer available");
			}
		}

		Order order = Order.builder()
				.user(user)
				.restaurant(restaurant)
				.status(OrderStatus.PENDING)
				.deliveryAddress(request.getDeliveryAddress().trim())
				.deliveryNotes(request.getDeliveryNotes() != null ? request.getDeliveryNotes().trim() : null)
				.totalAmount(cart.getTotalAmount())
				.items(new ArrayList<>())
				.build();

		for (var cartItem : cart.getItems()) {
			OrderItem orderItem = OrderItem.builder()
					.order(order)
					.menuItem(cartItem.getMenuItem())
					.itemName(cartItem.getMenuItem().getName())
					.quantity(cartItem.getQuantity())
					.unitPrice(cartItem.getUnitPrice())
					.subTotal(cartItem.getSubTotal())
					.build();
			order.getItems().add(orderItem);
		}

		Order savedOrder = orderRepository.save(order);

		// Clear cart after order is successfully placed
		cart.getItems().clear();
		cart.setTotalAmount(BigDecimal.ZERO);
		cartRepository.save(cart);

		return toResponse(savedOrder);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderResponse> getMyOrders(String customerEmail) {
		User user = userRepository.findByEmail(customerEmail)
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + customerEmail));

		return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
				.map(this::toResponse)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public OrderResponse getOrderById(Long orderId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
		return toResponse(order);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderResponse> getOrdersByRestaurant(Long restaurantId) {
		if (!restaurantRepository.existsById(restaurantId)) {
			throw new ResourceNotFoundException("Restaurant not found with id: " + restaurantId);
		}
		return orderRepository.findByRestaurantIdOrderByCreatedAtDesc(restaurantId).stream()
				.map(this::toResponse)
				.toList();
	}

	@Override
	public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

		if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED) {
			throw new IllegalArgumentException("Cannot change status of an order that is already " + order.getStatus());
		}

		order.setStatus(request.getStatus());
		Order updated = orderRepository.save(order);
		return toResponse(updated);
	}

	@Override
	public OrderResponse cancelOrder(Long orderId, String customerEmail) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

		if (!order.getUser().getEmail().equalsIgnoreCase(customerEmail)) {
			throw new IllegalArgumentException("You are not authorized to cancel this order");
		}

		if (order.getStatus() != OrderStatus.PENDING) {
			throw new IllegalArgumentException("Cannot cancel order in status: " + order.getStatus() + ". Only PENDING orders can be cancelled.");
		}

		order.setStatus(OrderStatus.CANCELLED);
		Order updated = orderRepository.save(order);
		return toResponse(updated);
	}

	private OrderResponse toResponse(Order order) {
		List<OrderItemResponse> itemResponses = order.getItems().stream()
				.map(item -> OrderItemResponse.builder()
						.id(item.getId())
						.menuItemId(item.getMenuItem() != null ? item.getMenuItem().getId() : null)
						.itemName(item.getItemName())
						.quantity(item.getQuantity())
						.unitPrice(item.getUnitPrice())
						.subTotal(item.getSubTotal())
						.build())
				.toList();

		return OrderResponse.builder()
				.id(order.getId())
				.userId(order.getUser().getId())
				.customerName(order.getUser().getFirstName() + " " + order.getUser().getLastName())
				.customerEmail(order.getUser().getEmail())
				.restaurantId(order.getRestaurant().getId())
				.restaurantName(order.getRestaurant().getName())
				.status(order.getStatus())
				.deliveryAddress(order.getDeliveryAddress())
				.deliveryNotes(order.getDeliveryNotes())
				.totalAmount(order.getTotalAmount())
				.createdAt(order.getCreatedAt())
				.updatedAt(order.getUpdatedAt())
				.items(itemResponses)
				.build();
	}
}

