package com.example.food.menuitem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.food.menuitem.entity.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

	@EntityGraph(attributePaths = {"menu"})
	@Query("""
			SELECT mi FROM MenuItem mi WHERE mi.menu.id = :menuId AND mi.available = true 
			""")
	List<MenuItem> findAvailableItemsByMenuId(@Param("menuId") Long menuId);
	
	@EntityGraph(attributePaths = {"menu"})
	@Query(
			"""
			SELECT mi FROM MenuItem mi WHERE mi.id = :itemId
			""")
	Optional<MenuItem> findByIdWithMenu (@Param("itemId") Long itemId);
	
	@EntityGraph(attributePaths = {"menu.restaurant.owner"})
	@Query("""
			SELECT CASE WHEN COUNT(mi) > 0 THEN true ELSE false END
			FROM MenuItem mi 
			WHERE mi.id = :itemId AND LOWER(mi.menu.restaurant.owner.email) = LOWER(:email)
			""")
	boolean isOwnedByUser(@Param("itemId") Long itemId, @Param("email") String email); 
}
