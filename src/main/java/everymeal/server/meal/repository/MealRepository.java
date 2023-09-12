package everymeal.server.meal.repository;


import everymeal.server.meal.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MealRepository extends JpaRepository<Meal, String> {}
