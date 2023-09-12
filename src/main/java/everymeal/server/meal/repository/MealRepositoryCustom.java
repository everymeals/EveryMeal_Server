package everymeal.server.meal.repository;


import everymeal.server.meal.entity.Meal;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepositoryCustom {
    List<Meal> findAllByOfferedAtToday(String restId);
}
