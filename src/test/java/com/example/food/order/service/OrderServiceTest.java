package com.example.food.order.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.food.cart.entity.Cart;
import com.example.food.cart.entity.CartItem;
import com.example.food.cart.repository.CartRepository;
import com.example.food.menuitem.entity.MenuItem;
import com.example.food.order.dto.CheckoutRequest;
import com.example.food.order.dto.OrderResponse;
import com.example.food.order.dto.UpdateOrderStatusRequest;
import com.example.food.order.entity.Order;
import com.example.food.order.entity.OrderStatus;
import com.example.food.order.repository.OrderRepository;
import com.example.food.restaurant.entity.Restaurant;
import com.example.food.restaurant.repository.RestaurantRepository;
import com.example.food.user.entity.User;
import com.example.food.user.repository.UserRepository;

class OrderServiceTest {

	private OrderRepository orderRepository;
	private CartRepository cartRepository;
	private UserRepository userRepository;
	private RestaurantRepository restaurantRepository;
	private OrderServiceImpl orderService;

	private User user;
	private Restaurant restaurant;
	private Cart cart;
	private MenuItem menuItem;

	private Map<Long, Order> savedOrders;
	private boolean cartSaved;

	@SuppressWarnings("unchecked")
	private <T> T createProxy(Class<T> iface, ProxyHandler handler) {
		return (T) Proxy.newProxyInstance(iface.getClassLoader(), new Class<?>[] { iface },
				(proxy, method, args) -> handler.handle(method.getName(), args));
	}

	@FunctionalInterface
	interface ProxyHandler {
		Object handle(String methodName, Object[] args) throws Throwable;
	}

	@BeforeEach
	void setUp() {
		savedOrders = new HashMap<>();
		cartSaved = false;

		user = new User();
		user.setId(1L);
		user.setEmail("test@example.com");
		user.setFirstName("John");
		user.setLastName("Doe");

		restaurant = new Restaurant();
		restaurant.setId(10L);
		restaurant.setName("Pizza Palace");
		restaurant.setActive(true);

		menuItem = new MenuItem();
		menuItem.setId(100L);
		menuItem.setName("Margherita");
		menuItem.setPrice(BigDecimal.valueOf(12.50));
		menuItem.setAvailable(true);

		cart = Cart.builder()
				.id(5L)
				.user(user)
				.restaurantId(10L)
				.totalAmount(BigDecimal.valueOf(25.00))
				.items(new ArrayList<>())
				.build();

		CartItem cartItem = CartItem.builder()
				.id(1L)
				.cart(cart)
				.menuItem(menuItem)
				.quantity(2)
				.unitPrice(BigDecimal.valueOf(12.50))
				.subTotal(BigDecimal.valueOf(25.00))
				.build();
		cart.getItems().add(cartItem);

		userRepository = createProxy(UserRepository.class, (method, args) -> {
			if ("findByEmail".equals(method)) {
				return "test@example.com".equals(args[0]) ? Optional.of(user) : Optional.empty();
			}
			return null;
		});

		cartRepository = createProxy(CartRepository.class, (method, args) -> {
			if ("findByUserId".equals(method)) {
				return Optional.of(cart);
			}
			if ("save".equals(method)) {
				cartSaved = true;
				return args[0];
			}
			return null;
		});

		restaurantRepository = createProxy(RestaurantRepository.class, (method, args) -> {
			if ("findById".equals(method)) {
				return Long.valueOf(10L).equals(args[0]) ? Optional.of(restaurant) : Optional.empty();
			}
			if ("existsById".equals(method)) {
				return Long.valueOf(10L).equals(args[0]);
			}
			return null;
		});

		orderRepository = createProxy(OrderRepository.class, (method, args) -> {
			if ("save".equals(method)) {
				Order o = (Order) args[0];
				if (o.getId() == null) {
					o.setId(99L);
				}
				savedOrders.put(o.getId(), o);
				return o;
			}
			if ("findById".equals(method)) {
				return Optional.ofNullable(savedOrders.get(args[0]));
			}
			if ("findByUserIdOrderByCreatedAtDesc".equals(method)) {
				return new ArrayList<>(savedOrders.values());
			}
			if ("findByRestaurantIdOrderByCreatedAtDesc".equals(method)) {
				return new ArrayList<>(savedOrders.values());
			}
			return null;
		});

		orderService = new OrderServiceImpl(orderRepository, cartRepository, userRepository, restaurantRepository);
	}

	@Test
	void checkout_successful() {
		CheckoutRequest request = CheckoutRequest.builder()
				.deliveryAddress("123 Food Street")
				.deliveryNotes("Ring the bell")
				.build();

		OrderResponse response = orderService.checkout("test@example.com", request);

		assertNotNull(response);
		assertEquals(99L, response.getId());
		assertEquals(OrderStatus.PENDING, response.getStatus());
		assertEquals("123 Food Street", response.getDeliveryAddress());
		assertEquals(1, response.getItems().size());
		assertEquals("Margherita", response.getItems().get(0).getItemName());
		assertEquals(BigDecimal.valueOf(25.00), response.getTotalAmount());

		// Verify cart is cleared
		assertTrue(cart.getItems().isEmpty());
		assertEquals(BigDecimal.ZERO, cart.getTotalAmount());
		assertTrue(cartSaved);
	}

	@Test
	void checkout_emptyCart_throwsException() {
		cart.getItems().clear();

		CheckoutRequest request = CheckoutRequest.builder()
				.deliveryAddress("123 Food Street")
				.build();

		assertThrows(IllegalArgumentException.class, () -> orderService.checkout("test@example.com", request));
	}

	@Test
	void checkout_inactiveRestaurant_throwsException() {
		restaurant.setActive(false);

		CheckoutRequest request = CheckoutRequest.builder()
				.deliveryAddress("123 Food Street")
				.build();

		assertThrows(IllegalArgumentException.class, () -> orderService.checkout("test@example.com", request));
	}

	@Test
	void checkout_itemUnavailable_throwsException() {
		menuItem.setAvailable(false);

		CheckoutRequest request = CheckoutRequest.builder()
				.deliveryAddress("123 Food Street")
				.build();

		assertThrows(IllegalArgumentException.class, () -> orderService.checkout("test@example.com", request));
	}

	@Test
	void cancelOrder_pending_successful() {
		Order order = Order.builder()
				.id(1L)
				.user(user)
				.restaurant(restaurant)
				.status(OrderStatus.PENDING)
				.totalAmount(BigDecimal.TEN)
				.deliveryAddress("123 St")
				.items(new ArrayList<>())
				.build();
		savedOrders.put(1L, order);

		OrderResponse response = orderService.cancelOrder(1L, "test@example.com");

		assertEquals(OrderStatus.CANCELLED, response.getStatus());
	}

	@Test
	void cancelOrder_notPending_throwsException() {
		Order order = Order.builder()
				.id(1L)
				.user(user)
				.restaurant(restaurant)
				.status(OrderStatus.PREPARING)
				.totalAmount(BigDecimal.TEN)
				.deliveryAddress("123 St")
				.items(new ArrayList<>())
				.build();
		savedOrders.put(1L, order);

		assertThrows(IllegalArgumentException.class, () -> orderService.cancelOrder(1L, "test@example.com"));
	}

	@Test
	void updateOrderStatus_successful() {
		Order order = Order.builder()
				.id(1L)
				.user(user)
				.restaurant(restaurant)
				.status(OrderStatus.PENDING)
				.totalAmount(BigDecimal.TEN)
				.deliveryAddress("123 St")
				.items(new ArrayList<>())
				.build();
		savedOrders.put(1L, order);

		OrderResponse response = orderService.updateOrderStatus(1L, new UpdateOrderStatusRequest(OrderStatus.PREPARING));

		assertEquals(OrderStatus.PREPARING, response.getStatus());
	}
}
