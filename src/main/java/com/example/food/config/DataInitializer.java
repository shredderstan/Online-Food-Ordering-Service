package com.example.food.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.food.menu.entity.Menu;
import com.example.food.menu.repository.MenuRepository;
import com.example.food.menuitem.entity.MenuItem;
import com.example.food.menuitem.repository.MenuItemRepository;
import com.example.food.restaurant.entity.Restaurant;
import com.example.food.restaurant.repository.RestaurantRepository;
import com.example.food.user.entity.User;
import com.example.food.user.entity.UserRole;
import com.example.food.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
	private final RestaurantRepository restaurantRepository;
	private final MenuRepository menuRepository;
	private final MenuItemRepository menuItemRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) {
		if (userRepository.count() > 0) {
			log.info("Database already contains data, skipping initialization.");
			return;
		}

		log.info("Seeding initial data for demonstration...");

		// 1. Users
		User customer = new User();
		customer.setFirstName("Alex");
		customer.setLastName("Morgan");
		customer.setEmail("customer@example.com");
		customer.setPhoneNumber("+1-555-0199");
		customer.setPassword(passwordEncoder.encode("password123"));
		customer.setRole(UserRole.CUSTOMER);
		customer = userRepository.save(customer);

		User owner = new User();
		owner.setFirstName("Marco");
		owner.setLastName("Rossi");
		owner.setEmail("owner@example.com");
		owner.setPhoneNumber("+1-555-0188");
		owner.setPassword(passwordEncoder.encode("password123"));
		owner.setRole(UserRole.RESTAURANT_OWNER);
		owner = userRepository.save(owner);

		User admin = new User();
		admin.setFirstName("System");
		admin.setLastName("Admin");
		admin.setEmail("admin@example.com");
		admin.setPhoneNumber("+1-555-0100");
		admin.setPassword(passwordEncoder.encode("admin123"));
		admin.setRole(UserRole.ADMIN);
		userRepository.save(admin);

		// 2. Restaurants
		Restaurant italian = Restaurant.builder()
				.name("Bella Italia Trattoria")
				.description("Authentic wood-fired Neapolitan pizzas, artisanal pasta, and classic Italian desserts.")
				.address("452 Bleecker Street")
				.city("New York")
				.phoneNumber("+1-555-0101")
				.owner(owner)
				.isActive(true)
				.build();
		italian = restaurantRepository.save(italian);

		Restaurant burgers = Restaurant.builder()
				.name("Burger & Co. Craft Shack")
				.description("Crispy smashed beef patties, brioche buns, golden fries, and thick hand-spun milkshakes.")
				.address("108 Bedford Avenue")
				.city("Brooklyn")
				.phoneNumber("+1-555-0102")
				.owner(owner)
				.isActive(true)
				.build();
		burgers = restaurantRepository.save(burgers);

		Restaurant sushi = Restaurant.builder()
				.name("Sakura Sushi & Ramen")
				.description("Fresh sashimi, specialty sushi rolls, and slow-simmered rich tonkotsu ramen bowls.")
				.address("77-01 Queens Blvd")
				.city("Queens")
				.phoneNumber("+1-555-0103")
				.owner(owner)
				.isActive(true)
				.build();
		sushi = restaurantRepository.save(sushi);

		// 3. Menus & Items for Bella Italia
		Menu italianMenu = Menu.builder()
				.restaurant(italian)
				.name("Pizzas & Pasta")
				.description("Chef's selection of traditional Italian specialties")
				.build();
		italianMenu = menuRepository.save(italianMenu);

		MenuItem pizza = MenuItem.builder()
				.menu(italianMenu)
				.name("Margherita D.O.P.")
				.description("San Marzano tomato sauce, fresh mozzarella di bufala, basil, and extra virgin olive oil.")
				.price(BigDecimal.valueOf(15.50))
				.available(true)
				.build();

		MenuItem pasta = MenuItem.builder()
				.menu(italianMenu)
				.name("Truffle Tagliatelle")
				.description("Handmade ribbon pasta tossed with black summer truffle butter and Parmigiano-Reggiano.")
				.price(BigDecimal.valueOf(18.99))
				.available(true)
				.build();

		MenuItem tiramisu = MenuItem.builder()
				.menu(italianMenu)
				.name("Classic Tiramisu")
				.description("Espresso-soaked ladyfingers layered with whipped mascarpone cream and dusted with cocoa.")
				.price(BigDecimal.valueOf(8.50))
				.available(true)
				.build();
		menuItemRepository.saveAll(List.of(pizza, pasta, tiramisu));

		// 4. Menus & Items for Burger & Co.
		Menu burgerMenu = Menu.builder()
				.restaurant(burgers)
				.name("Burgers & Sides")
				.description("Signature craft burgers and crunchy sides")
				.build();
		burgerMenu = menuRepository.save(burgerMenu);

		MenuItem smashBurger = MenuItem.builder()
				.menu(burgerMenu)
				.name("The Double Smash Classic")
				.description("Two Angus patties, melted American cheese, caramelized onions, house pickles, and secret sauce.")
				.price(BigDecimal.valueOf(13.25))
				.available(true)
				.build();

		MenuItem fries = MenuItem.builder()
				.menu(burgerMenu)
				.name("Rosemary Parmesan Fries")
				.description("Crispy hand-cut potatoes dusted with fresh rosemary, parmesan, and roasted garlic aioli.")
				.price(BigDecimal.valueOf(5.50))
				.available(true)
				.build();

		MenuItem shake = MenuItem.builder()
				.menu(burgerMenu)
				.name("Salted Caramel Pretzel Shake")
				.description("Hand-spun vanilla custard blended with salted caramel and pretzel crunch.")
				.price(BigDecimal.valueOf(6.50))
				.available(true)
				.build();
		menuItemRepository.saveAll(List.of(smashBurger, fries, shake));

		// 5. Menus & Items for Sakura Sushi
		Menu sushiMenu = Menu.builder()
				.restaurant(sushi)
				.name("Sushi & Bowls")
				.description("Freshly rolled sushi and hot steaming ramen")
				.build();
		sushiMenu = menuRepository.save(sushiMenu);

		MenuItem ramen = MenuItem.builder()
				.menu(sushiMenu)
				.name("Spicy Tonkotsu Ramen")
				.description("Rich pork broth, chashu pork belly, ajitsuke tamago egg, bamboo shoots, and scallions.")
				.price(BigDecimal.valueOf(16.75))
				.available(true)
				.build();

		MenuItem sushiRoll = MenuItem.builder()
				.menu(sushiMenu)
				.name("Rainbow Dragon Roll")
				.description("Eel and cucumber topped with avocado, tuna, salmon, tobiko, and unagi glaze.")
				.price(BigDecimal.valueOf(17.50))
				.available(true)
				.build();
		menuItemRepository.saveAll(List.of(ramen, sushiRoll));

		log.info("Sample restaurants and menus seeded successfully.");
	}
}

