package com.example.food.restaurant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.food.restaurant.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>{
	
	List<Restaurant> findByOwnerId(Long ownerId);
	
	List<Restaurant> findByCityIgnoreCase(String city);
	
	List<Restaurant> findByIsActiveTrue();
	
	boolean existsByIdAndOwnerEmailIgnoreCase(Long restaurantId, String email);
	
	@Query("SELECT r FROM Restaurant r JOIN FETCH r.owner WHERE r.id = :id")
	Optional<Restaurant> findByIdWithOwner(@Param("id") Long id);
	

}
