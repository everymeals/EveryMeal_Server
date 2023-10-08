package everymeal.server.meal.repository;


import everymeal.server.meal.controller.dto.request.MealRegisterReq;
import everymeal.server.meal.entity.Meal;
import everymeal.server.meal.entity.MealType;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepositoryCustom {
    List<Meal> findAllByAfterOfferedAt(MealRegisterReq mealRegisterReq, Long restaurantIdx);

    List<Meal> findAllByOfferedAtOnDateAndMealType(
            Instant offeredAt, MealType mealType, Long restaurantIdx);
}
