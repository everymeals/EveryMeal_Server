package everymeal.server.meal.repository;


import everymeal.server.meal.controller.dto.request.MealRegisterReq;
import everymeal.server.meal.entity.Meal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepositoryCustom {
    List<Meal> findAllByAfterOfferedAt(MealRegisterReq mealRegisterReq, Long restaurantIdx);

    List<Meal> findAllByOfferedAt(LocalDate offeredAt, Long restaurantIdx);

    List<Meal> findAllByBetweenOfferedAtAndEndedAt(
            LocalDate startedAt, LocalDate endedAt, Long restaurantIdx);
}
