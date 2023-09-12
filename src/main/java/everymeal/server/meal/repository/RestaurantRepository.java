package everymeal.server.meal.repository;


import everymeal.server.meal.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, String> {}
