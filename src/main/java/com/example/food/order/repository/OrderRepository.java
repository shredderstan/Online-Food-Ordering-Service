package com.example.food.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.food.order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

	List<Order> findByRestaurantIdOrderByCreatedAtDesc(Long restaurantId);

	@Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM Order o WHERE o.id = :orderId AND o.user.email = :email")
	boolean isCustomerOwner(@Param("orderId") Long orderId, @Param("email") String email);

	@Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM Order o WHERE o.id = :orderId AND o.restaurant.owner.email = :email")
	boolean isRestaurantOwnerForOrder(@Param("orderId") Long orderId, @Param("email") String email);
}

