package everymeal.server.meal.repository;


import everymeal.server.meal.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, String> {}
