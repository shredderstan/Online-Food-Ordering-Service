package com.example.food.menu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.food.menu.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long>{
	
	List<Menu>findByRestaurantIdAndActiveTrue(Long restaurantId);
	
	List<Menu> findByRestaurantId(Long restaurantId);
	
	@Query("""
			SELECT COUNT(m) > 0 FROM Menu m 
			JOIN m.restaurant r JOIN r.owner o WHERE m.id = :menuId AND LOWER(o.email) = LOWER(:email)
			""")
	boolean isOwnedByUser(@Param("menuId") Long menuId, @Param("email") String email);

}
