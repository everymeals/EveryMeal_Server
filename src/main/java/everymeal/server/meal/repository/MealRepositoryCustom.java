package everymeal.server.meal.repository;


import everymeal.server.meal.controller.dto.response.DayMealListGetRes;
import everymeal.server.meal.controller.dto.response.WeekMealListGetRes;
import everymeal.server.meal.entity.Meal;
import everymeal.server.meal.entity.MealType;
import everymeal.server.meal.entity.Restaurant;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepositoryCustom {
    List<Meal> findAllByOfferedAtOnDateAndMealType(
            LocalDate offeredAt, MealType mealType, Restaurant restaurant);

    List<DayMealListGetRes> findAllByOfferedAtOnDate(LocalDate offeredAt, Restaurant restaurant);

    List<WeekMealListGetRes> getWeekMealList(
            Restaurant restaurant, LocalDate mondayInstant, LocalDate sundayInstant);
}
